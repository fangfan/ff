package com.adchina.dbutil.sql.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.adchina.dbutil.condition.Condition;
import com.adchina.dbutil.condition.Condition.Builder;
import com.adchina.dbutil.exception.NestedComponentException;
import com.adchina.dbutil.sql.AggregationType;
import com.adchina.dbutil.sql.OrderByType;
import com.adchina.dbutil.sql.SeperatorSymbol;
import com.adchina.dbutil.sql.SqlKeyWordLocation;
import com.adchina.dbutil.sql.SqlKeyWords;
import com.adchina.dbutil.sql.TableInfo;
import com.adchina.dbutil.sql.keys.SelectSqlKey;
import com.adchina.dbutil.sql.tree.AbstractSqlTree;
import com.adchina.dbutil.sql.tree.QuerySqlTree;
import com.adchina.dbutil.sqlparser.RegexConstants;

/**
 * Select 查询语句解析器 1,依据正则表达式匹配将sql解析成片段. 2,对各部分执行检查和解析.
 * 正则表达式@link(com.adchina.dbutil.sqlparser.RegexConstants)
 * 
 * @author F.Fang
 * 
 */
public class QuerySqlParser extends SqlParser {

    private Map<String, Integer> keyWordsIndexOrder;

    public QuerySqlParser(String sql) {
        super(sql);
        initKeyWordOrder();
    }

    private void initKeyWordOrder() {
        keyWordsIndexOrder = new HashMap<String, Integer>();
        // keyWordsIndexOrder.put(SqlKeyWords.SELECT, 0);
        keyWordsIndexOrder.put(SqlKeyWords.FROM, 0);
        keyWordsIndexOrder.put(SqlKeyWords.WHERE, 1);
        keyWordsIndexOrder.put(SqlKeyWords.GROUP_BY, 2);
        keyWordsIndexOrder.put(SqlKeyWords.ORDER_BY, 3);
        keyWordsIndexOrder.put(SqlKeyWords.LIMIT, 4);
        keyWordsIndexOrder.put(SqlKeyWords.ENDSQL, 5);
    }

    @Override
    public AbstractSqlTree parse() {
        // 执行* 或者.* 检查
        if (sql.contains(SeperatorSymbol.STAR) || sql.contains(SeperatorSymbol.POINTSTAR)) {
            throw new NestedComponentException("sql语句不合法,不允许使用[.*]或[*] !");
        }
        // 解析出关键字,字段名,表名,等信息.
        // 定义语法树
        QuerySqlTree sqlTree = new QuerySqlTree(sql);
        // 各种生成的条件.
        Builder builder = new Condition.Builder();

        // 关键字from where group by order by limit顺序表.
        List<SqlKeyWordLocation> keyWordLocations = new ArrayList<SqlKeyWordLocation>(keyWordsIndexOrder.size());

        // 1.执行关键字匹配 每个关键字都应该执行非空检查.
        // (a) 选择字段.
        matchSelectFromAndFillSelectFields(sqlTree, keyWordLocations);
        // (b) where关键字.
        keyWordLocations.add(matchWhereRegex());
        // (c) group by 关键字.
        keyWordLocations.add(matchGroupByRegex());
        // (d) order by 关键字.
        keyWordLocations.add(matchOrderByRegex());
        // (e) limit 关键字.
        keyWordLocations.add(matchLimitRegex());
        
        //填充末尾设定的Location
        keyWordLocations.add(new SqlKeyWordLocation(SqlKeyWords.ENDSQL,sql.length(),sql.length()));

        // 2.执行关键字顺序检查.
        // (a),检查关键字顺序表,若不通过,则抛出异常!.
        int[] keyWordsOrder = getKeyWordsStartIndexs(keyWordLocations);
        checkKeyWordsOrder(keyWordsOrder);
        // (b),检查空格.
        checkKeyWordsBlank(keyWordLocations);

        // 3.填充表信息.
        fillTableInfo(sqlTree, keyWordLocations);

        // 4.解析选择字段和distinct及聚合函数.
        parseFields(sqlTree, builder);

        // 5.where条件处理 填充where连接条件.
        fillJoinCondition(sqlTree, keyWordLocations);

        // 6.group by关键字处理.
        // 处理group by 具体的字段暂不验证
        parseGroupByInfo(sqlTree, builder, keyWordLocations);

        // 7.处理 Order by 具体的字段暂不验证
        parseOrderByField(sqlTree, builder, keyWordLocations);

        // 8.处理limit 分页
        parsePagingInfo(builder, keyWordLocations);

        sqlTree.setOrignalSql(sql);
        sqlTree.setCondition(builder.build());
        return sqlTree;
    }

    /**
     * 检查关键字前后的空格.
     */
    private void checkKeyWordsBlank(List<SqlKeyWordLocation> keyWordLocations) {
        for (int i=0,len = keyWordLocations.size(); i<len-1; ++i) {
            SqlKeyWordLocation location = keyWordLocations.get(i);
            if (location.getStart() == -1) {
                break;
            }
            // 验证关键字之前的空格
            if (sql.charAt(location.getStart() - 1) != SeperatorSymbol.EMPTY.charAt(0)) {
                throw new NestedComponentException("select sql语句不合法," + location.getKeyword() + "关键字之前必须是一个空格!");
            }
            // 验证关键字之后的内容是否为空.
            if (location.getEnd() == sql.length() || StringUtils.isBlank(sql.substring(location.getEnd()))) {
                throw new NestedComponentException("select sql语句不合法," + location.getKeyword() + "关键字之后的信息为空!");
            }
            // 验证关键字之后的空格
            if (sql.charAt(location.getEnd()) != SeperatorSymbol.EMPTY.charAt(0)) {
                throw new NestedComponentException("select sql语句不合法," + location.getKeyword() + "关键字之后必须紧跟一个空格!");
            }
        }
    }

    /**
     * 获取关键字起始位置数组.
     * 
     * @param keyWordLocations
     * @return
     */
    private int[] getKeyWordsStartIndexs(List<SqlKeyWordLocation> keyWordLocations) {
        int size = keyWordLocations.size();
        int[] keyWordsOrder = new int[size];
        for (int i = 0; i < size; ++i) {
            keyWordsOrder[i] = keyWordLocations.get(i).getStart();
        }
        return keyWordsOrder;
    }

    /**
     * 解析limit分页信息.
     * 
     * @param builder
     * @param keyWordLocations
     */
    private void parsePagingInfo(Builder builder, List<SqlKeyWordLocation> keyWordLocations) {
        int nowIndex = keyWordsIndexOrder.get(SqlKeyWords.LIMIT);
        SqlKeyWordLocation limitLocation = keyWordLocations.get(nowIndex);
        if (limitLocation.getStart() != -1) {
            // limit属于最后一个关键字.
            // 分页处理时也需要做验证.
            SqlKeyWordLocation neighborLocation = getNeighborKeyWordLocation(keyWordLocations, nowIndex);
            String pageSettings = sql.substring(limitLocation.getEnd(), neighborLocation.getStart());

            String[] pageSettingsArr = pageSettings.split(SeperatorSymbol.COMMA);

            try {
                int startIndex = -1;
                int offSet = -2;
                if (pageSettingsArr.length == 2) {
                    startIndex = Integer.parseInt(pageSettingsArr[0].trim());
                    // offSet允许为-1
                    offSet = Integer.parseInt(pageSettingsArr[1].trim());
                } else if (pageSettingsArr.length == 1) {
                    startIndex = 0;
                    offSet = Integer.parseInt(pageSettingsArr[0].trim());
                }
                if (startIndex < 0 || offSet < -1) {
                    throw new NestedComponentException("sql语句不合法,limit关键字分页设置不正确,起始位置和偏移量大小不合法!");
                }
                builder.paging(startIndex, offSet);
            } catch (Exception e) {
                // 可能是数字转换错误.
                throw new NestedComponentException("sql语句不合法,limit关键字分页设置值有误," + e.getMessage());
            }
        }
    }

    /**
     * 解析Order by信息.
     * 
     * @param sqlTree
     * @param builder
     * @param keyWordLocations
     */
    private void parseOrderByField(QuerySqlTree sqlTree, Builder builder, List<SqlKeyWordLocation> keyWordLocations) {
        int nowIndex = keyWordsIndexOrder.get(SqlKeyWords.ORDER_BY);
        SqlKeyWordLocation orderByLocation = keyWordLocations.get(nowIndex);
        if (orderByLocation.getStart() != -1) {
            SqlKeyWordLocation neighborLocation = getNeighborKeyWordLocation(keyWordLocations, nowIndex);
            String orderByFields = sql.substring(orderByLocation.getEnd(), neighborLocation.getStart());
            String[] orderByFieldsArr = orderByFields.split(SeperatorSymbol.COMMA);
            StringBuffer orderByFieldBuf = new StringBuffer();
            for (String field : orderByFieldsArr) {
                String upperField = field.toUpperCase();
                if (upperField.contains(OrderByType.ASC.name())) {
                    int ascIndex = upperField.indexOf(OrderByType.ASC.name());
                    String orderByField = field.substring(0, ascIndex).trim();
                    SelectSqlKey selectSqlKey = findSelectSqlKeyFieldExists(sqlTree, orderByField);
                    if (selectSqlKey!=null) {
                        builder.orderBy(selectSqlKey.getResultSetKey(), OrderByType.ASC);
                        orderByFieldBuf.append(orderByField+SeperatorSymbol.EMPTY+OrderByType.ASC.name());
                        orderByFieldBuf.append(SeperatorSymbol.COMMA);
                    }
                } else if (upperField.contains(OrderByType.DESC.name())) {
                    int descIndex = upperField.indexOf(OrderByType.DESC.name());
                    String orderByField = field.substring(0, descIndex).trim();
                    SelectSqlKey selectSqlKey = findSelectSqlKeyFieldExists(sqlTree, orderByField);
                    if (selectSqlKey!=null) {
                        builder.orderBy(selectSqlKey.getResultSetKey(), OrderByType.DESC);
                        orderByFieldBuf.append(orderByField+SeperatorSymbol.EMPTY+OrderByType.DESC.name());
                        orderByFieldBuf.append(SeperatorSymbol.COMMA);
                    }
                } else {
                    String orderByField = field.trim();
                    SelectSqlKey selectSqlKey = findSelectSqlKeyFieldExists(sqlTree, orderByField);
                    if (selectSqlKey!=null) {
                        builder.orderBy(selectSqlKey.getResultSetKey(), OrderByType.ASC);
                        orderByFieldBuf.append(orderByField+SeperatorSymbol.EMPTY+OrderByType.ASC.name());
                        orderByFieldBuf.append(SeperatorSymbol.COMMA);
                    }
                }
            }
            sqlTree.setOrderByFields(orderByFieldBuf.substring(0, orderByFieldBuf.length()-1));
        }
    }

    /**
     * 解析group by信息.
     * 
     * @param sqlTree
     * @param builder
     * @param keyWordLocations
     */
    private void parseGroupByInfo(QuerySqlTree sqlTree, Builder builder, List<SqlKeyWordLocation> keyWordLocations) {
        int nowIndex = keyWordsIndexOrder.get(SqlKeyWords.GROUP_BY);
        SqlKeyWordLocation groupByLocation = keyWordLocations.get(nowIndex);
        if (groupByLocation.getStart() != -1) {
            SqlKeyWordLocation neighborLocation = getNeighborKeyWordLocation(keyWordLocations, nowIndex);
            String groupByFields = sql.substring(groupByLocation.getEnd(), neighborLocation.getStart());
            StringBuffer groupByFieldsBuf = new StringBuffer();
            String[] groupByFieldsArr = groupByFields.split(SeperatorSymbol.COMMA);
            for (String field : groupByFieldsArr) {
                // 字段不允许出现函数,具体数据库执行时若出现函数也将失败,同时去掉空格
                // 如果选择字段中不包含 group by字段,则不作为二次group by字段.
                // select id,max(password) from user group by id,name;
                // 1,各个db执行:select id,max(password) from user group by id,name;
                // 2,数据汇总时执行:select id,max(password) from user group by id;
                // name字段不出现在选择字段中,对汇总没有影响.
                String groupByField = field.trim();
                // field 中不允许出现聚合函数, 此处不检查.
                SelectSqlKey selectSqlKey = findSelectSqlKeyFieldExists(sqlTree, groupByField);
                if (selectSqlKey != null) {
                    builder.groupBy(selectSqlKey.getResultSetKey());
                    groupByFieldsBuf.append(groupByField+SeperatorSymbol.COMMA);
                }
            }
            sqlTree.setGroupByFields(groupByFieldsBuf.substring(0, groupByFieldsBuf.length()-1));
        }
    }

    /**
     * 检查group by 和 order by的字段是否在原有的select字段中存在!
     * 
     * @param sqlTree
     * @param field
     * @return
     */
    private SelectSqlKey findSelectSqlKeyFieldExists(QuerySqlTree sqlTree, String field) {
        SelectSqlKey result = null;
        for (SelectSqlKey selectKey : sqlTree.getSelectSqlKeys()) {
            if (field.equals(selectKey.getKey())||field.equals(selectKey.getAliasKey())) {
                result = selectKey;
                break;
            }
        }
        return result;
    }

    /**
     * 填充where连接条件.
     * 
     * @param sqlTree
     * @param keyWordsOrder
     */
    private void fillJoinCondition(QuerySqlTree sqlTree, List<SqlKeyWordLocation> keyWordLocations) {
        int nowIndex = keyWordsIndexOrder.get(SqlKeyWords.WHERE);
        SqlKeyWordLocation whereLocation = keyWordLocations.get(nowIndex);
        if (whereLocation.getStart() != -1) {
            SqlKeyWordLocation neighborLocation = getNeighborKeyWordLocation(keyWordLocations, nowIndex);
            // 分离查询条件.
            sqlTree.setJoinCondition(sql.substring(whereLocation.getEnd(), neighborLocation.getStart()));
        }
    }

    /**
     * 填充表信息. 寻找from关键字之后的首个关键字,若无则至sql末尾. 若存在,则from与该关键字之间的位置即为相关的解析片段.
     * 
     * @param sqlTree
     * @param keyWordsOrder
     *            关键字顺序表
     */
    private void fillTableInfo(QuerySqlTree sqlTree, List<SqlKeyWordLocation> keyWordLocations) {
        // 获取邻接关键字的起始位置.
        int nowIndex = keyWordsIndexOrder.get(SqlKeyWords.FROM);
        SqlKeyWordLocation fromLocation = keyWordLocations.get(nowIndex);
        SqlKeyWordLocation neighborLocation = getNeighborKeyWordLocation(keyWordLocations, nowIndex);

        // 分离表信息.
        String tableInfo = sql.substring(fromLocation.getEnd(), neighborLocation.getStart());
        sqlTree.setTableInfo(tableInfo);
        // 表之间用逗号隔开.
        String[] tbInfos = tableInfo.split(SeperatorSymbol.COMMA);
        List<TableInfo> tableInfoList = new ArrayList<TableInfo>();
        for (String tbInfo : tbInfos) {
            TableInfo tbi = new TableInfo();
            int pointIndex = tbInfo.indexOf(SeperatorSymbol.POINT);
            if (pointIndex != -1) {
                tbi.setAlias(tbInfo.substring(0, pointIndex).trim());
                tbi.setName(tbInfo.substring(pointIndex + 1).trim());
            } else {
                tbi.setName(tbInfo.trim());
            }
            tableInfoList.add(tbi);
        }
        sqlTree.setTablesInfo(tableInfoList);
    }

    /**
     * 分离选择字段.
     * 
     * @param sqlTree
     * @param builder
     */
    private void parseFields(QuerySqlTree sqlTree, Builder builder) {
        String selectFields = sqlTree.getSelectFields();
        String upperSelectFields = selectFields.toUpperCase();
        // 如果包含distinct关键字.
        int distinctStartIndex = upperSelectFields.indexOf(SqlKeyWords.DISTINCT);

        if (distinctStartIndex != -1) {
            int distinctEndIndex = distinctStartIndex + 8;
            // 设置distinct
            sqlTree.setDistinct(true);
            builder.distinct();

            // 处理选择字段,并分离聚合函数.
            scanAggregateFunction(builder, selectFields.substring(distinctEndIndex), sqlTree);
        } else {
            // 处理选择字段,并分离聚合函数.
            scanAggregateFunction(builder, selectFields, sqlTree);
        }

        //
        for (SelectSqlKey selectSqlKey : sqlTree.getSelectSqlKeys()) {
            addAggregateFunction(builder, selectSqlKey);
        }
    }

    /**
     * 在实际的各字段匹配中,如果包含聚合函数,实际上还需要检查 "(" ")"的匹配问题.
     * 
     * @param builder
     * @param selectFields
     * @param sqlTree
     */
    private void scanAggregateFunction(Builder builder, String selectFields, QuerySqlTree sqlTree) {
        if (StringUtils.isBlank(selectFields)) {
            throw new NestedComponentException("select sql语句不合法,选择字段不允许为空!");
        }

        List<SelectSqlKey> selectSqlKeys = new ArrayList<SelectSqlKey>();

        // ,分割.
        String[] fields = selectFields.split(SeperatorSymbol.COMMA);
        for (String field : fields) {
            SelectSqlKey selectSqlKey = new SelectSqlKey();
            // 聚合函数起始位置. 也用于判定当前field是否包含聚合函数.
            int typeStartIndex = -1;
            String upperField = field.toUpperCase();
            // 搜索as
            int asIndex = upperField.indexOf(SqlKeyWords.AS);
            String fieldNameWithTableAlias = field;
            while (asIndex != -1) {
                // 当前后均出现空格 才可以当as来处理.
                if (upperField.charAt(asIndex - 1) == SeperatorSymbol.EMPTY.charAt(0)
                                && upperField.charAt(asIndex + 2) == SeperatorSymbol.EMPTY.charAt(0)) {
                    // 存在as
                    selectSqlKey.setHasAs(true);
                    selectSqlKey.setAliasKey(field.substring(asIndex + 3, field.length()).trim());
                    fieldNameWithTableAlias = field.substring(0, asIndex);
                    break;
                }
                asIndex+=2;
                if(asIndex>upperField.length()){
                    asIndex = upperField.length();
                }
                asIndex = upperField.indexOf(SqlKeyWords.AS,asIndex);
            }

            for (AggregationType type : AggregationType.values()) {
                String typeName = type.name();
                typeStartIndex = upperField.indexOf(typeName + SeperatorSymbol.LEFT_PARENTHESIS);
                if (typeStartIndex != -1) {
                    // 设置聚合函数.
                    selectSqlKey.setType(type);
                    int left = upperField.indexOf(SeperatorSymbol.LEFT_PARENTHESIS, typeStartIndex);
                    int right = upperField.indexOf(SeperatorSymbol.RIGHT_PARENTHESIS, typeStartIndex);
                    // ()之间的字符.
                    fieldNameWithTableAlias = field.substring(left + 1, right);
                    // 聚合函数不可能重叠出现,因此搜索成功一次后就完成了.
                    break;
                }
            }
            // 若存在聚合函数 selectSqlKey的表别名&字段名均已被赋值.
            // 检查有无表别名.
            int pointIndex = fieldNameWithTableAlias.indexOf(SeperatorSymbol.POINT);
            if (pointIndex != -1) {
                selectSqlKey.setTbAlias(fieldNameWithTableAlias.substring(0, pointIndex).trim());
                selectSqlKey.setKey(fieldNameWithTableAlias.substring(pointIndex + 1).trim());
            } else {
                selectSqlKey.setKey(fieldNameWithTableAlias.trim());
            }
            selectSqlKeys.add(selectSqlKey);
        }

        sqlTree.setSelectSqlKeys(selectSqlKeys);
        builder.fillSelectSqlKeys(selectSqlKeys);
        // 设置新的选择字段.
    }

    /**
     * 填充聚合函数.
     * 
     * @param builder
     * @param fieldName
     * @param type
     */
    private void addAggregateFunction(Builder builder, SelectSqlKey selectSqlKey) {
        AggregationType type = selectSqlKey.getType();
        if (type != null) {
            switch (type) {
            case COUNT:
                builder.count(selectSqlKey.getResultSetKey());
                break;
            case MAX:
                builder.max(selectSqlKey.getResultSetKey());
                break;
            case MIN:
                builder.min(selectSqlKey.getResultSetKey());
                break;
            case SUM:
                builder.sum(selectSqlKey.getResultSetKey());
                break;
            default:
                break;
            }
        }
    }

    /**
     * 检查关键字顺序,若语句中以下关键字均存在,则保证顺序为. from, where, group by, order by, limit.
     */
    private void checkKeyWordsOrder(int[] keyWordsOrder) {
        // 预留在之后实现 要保证数组过滤掉数字为-1的值后,从左至右数字从小到大.
        Arrays.copyOf(keyWordsOrder, keyWordsOrder.length);

        // 原始的数组
        int len = keyWordsOrder.length;

        // 复制原始数组.
        int[] keyWordsOrderCopy = Arrays.copyOf(keyWordsOrder, len);

        // 对复制数组排序.
        Arrays.sort(keyWordsOrderCopy);

        // 从原始数组里过滤 为-1的数字.
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        for (int i = 0; i < len; ++i) {
            if (keyWordsOrder[i] != -1) {
                sb1.append(keyWordsOrder[i]);
            }
            if (keyWordsOrderCopy[i] != -1) {
                sb2.append(keyWordsOrderCopy[i]);
            }
        }
        if (!sb1.toString().equals(sb2.toString())) {
            throw new NestedComponentException(
                            "select sql语句不合法,各关键字位置顺序不正确,默认顺序(select, distinct, from, where, group by, order by, limit)!");
        }
    }

    /**
     * 匹配Limit.
     * 
     * @param keyWordsOrder
     *            关键字顺序表
     */
    private SqlKeyWordLocation matchLimitRegex() {
        Matcher limitMatcher = buildMatcher(RegexConstants.SELECT_LIMIT);
        SqlKeyWordLocation keyWordLocation = null;
        if (limitMatcher.find()) {
            // keyWordsOrder[index] =
            // limitMatcher.end()-keyWordsNames.get(index).length();
            int end = limitMatcher.end();
            int start = end - SqlKeyWords.LIMIT.length();
            keyWordLocation = new SqlKeyWordLocation(SqlKeyWords.LIMIT, start, end);
        } else {
            // keyWordsOrder[index] = -1;
            keyWordLocation = new SqlKeyWordLocation(SqlKeyWords.LIMIT);
        }
        return keyWordLocation;
    }

    /**
     * 匹配Order By
     * 
     * @param keyWordsOrder
     *            关键字顺序表
     */
    private SqlKeyWordLocation matchOrderByRegex() {
        Matcher orderByMatcher = buildMatcher(RegexConstants.SELECT_ORDER_BY);
        SqlKeyWordLocation keyWordLocation = null;
        if (orderByMatcher.find()) {
            // keyWordsOrder[index] =
            // orderByMatcher.end()-keyWordsNames.get(index).length();
            int end = orderByMatcher.end();
            int start = end - SqlKeyWords.ORDER_BY.length();
            keyWordLocation = new SqlKeyWordLocation(SqlKeyWords.ORDER_BY, start, end);
        } else {
            // keyWordsOrder[index] = -1;
            keyWordLocation = new SqlKeyWordLocation(SqlKeyWords.ORDER_BY);
        }
        return keyWordLocation;
    }

    /**
     * 匹配Group By
     * 
     * @param keyWordsOrder
     *            关键字顺序表
     */
    private SqlKeyWordLocation matchGroupByRegex() {
        Matcher groupByMatcher = buildMatcher(RegexConstants.SELECT_GROUP_BY);
        SqlKeyWordLocation keyWordLocation = null;
        if (groupByMatcher.find()) {
            // keyWordsOrder[index] =
            // groupByMatcher.end()-keyWordsNames.get(index).length();
            int end = groupByMatcher.end();
            int start = end - SqlKeyWords.GROUP_BY.length();
            keyWordLocation = new SqlKeyWordLocation(SqlKeyWords.GROUP_BY, start, end);
        } else {
            // keyWordsOrder[index] = -1;
            keyWordLocation = new SqlKeyWordLocation(SqlKeyWords.GROUP_BY);
        }
        return keyWordLocation;
    }

    /**
     * 匹配where.
     * 
     * @param keyWordsOrder
     *            关键字顺序表
     */
    private SqlKeyWordLocation matchWhereRegex() {
        Matcher whereMatcher = buildMatcher(RegexConstants.SELECT_WHERE);
        SqlKeyWordLocation keyWordLocation = null;
        if (whereMatcher.find()) {
            int end = whereMatcher.end();
            int start = end - SqlKeyWords.WHERE.length();
            keyWordLocation = new SqlKeyWordLocation(SqlKeyWords.WHERE, start, end);
        } else {
            keyWordLocation = new SqlKeyWordLocation(SqlKeyWords.WHERE);
        }
        return keyWordLocation;
    }

    /**
     * 匹配基本的select语句. from关键字作为第一个顺序关键字.
     * 
     * @param sqlTree
     * @param keyWordsOrder
     */
    private void matchSelectFromAndFillSelectFields(QuerySqlTree sqlTree, List<SqlKeyWordLocation> keyWordLocations) {
        Matcher selectMatcher = buildMatcher(RegexConstants.SELECT_BASE);
        if (!selectMatcher.find()) {
            throw new NestedComponentException("select sql语句不合法,关键字缺失或空格缺失或语句不完整!");
        }

        int selectStartIndex = selectMatcher.start();
        int selectEndIndex = selectStartIndex + SqlKeyWords.SELECT.length();
        if (StringUtils.isNotBlank(sql.substring(0, selectStartIndex))) {
            throw new NestedComponentException("sql语句 select关键字之前不应该出现字符!");
        }

        if (sql.charAt(selectEndIndex) != SeperatorSymbol.EMPTY.charAt(0)) {
            throw new NestedComponentException("sql语句 select关键字之后必须有空格!");
        }

        int fromEndIndex = selectMatcher.end();
        if (StringUtils.isBlank(sql.substring(fromEndIndex))) {
            throw new NestedComponentException("sql语句 from关键字之后必须有表名!");
        }

        String selectFields = selectMatcher.group(2);
        sqlTree.setSelectFields(selectFields);
        // from关键字起始位置.

        SqlKeyWordLocation fromLoction = new SqlKeyWordLocation(SqlKeyWords.FROM, fromEndIndex
                        - SqlKeyWords.FROM.length(), fromEndIndex);
        keyWordLocations.add(fromLoction);
    }

    /**
     * 获取临接的关键字位置. 预定义关键字"ENDSQL"代表sql末尾.
     * 
     * @param keyWordLocations
     * @param nowIndex
     * @return
     */
    private SqlKeyWordLocation getNeighborKeyWordLocation(List<SqlKeyWordLocation> keyWordLocations, int nowIndex) {
        int size = keyWordLocations.size();
        SqlKeyWordLocation keyWordLocation = null;
        for (int i = nowIndex + 1; i < size; ++i) {
            if (keyWordLocations.get(i).getStart() != -1) {
                keyWordLocation = keyWordLocations.get(i);
                break;
            }
        }
        return keyWordLocation;
    }

    private Matcher buildMatcher(String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        return matcher;
    }

}
