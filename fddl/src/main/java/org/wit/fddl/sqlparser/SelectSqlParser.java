package org.wit.fddl.sqlparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.wit.fddl.condition.Condition;
import org.wit.fddl.exception.NestedComponentException;
import org.wit.fddl.sql.AggregationType;
import org.wit.fddl.sql.OrderByType;
import org.wit.fddl.sql.SeperatorSymbol;
import org.wit.fddl.sql.SqlKeyWords;
import org.wit.fddl.sql.SqlTree;
import org.wit.fddl.sql.SqlType;

/**
 * Select 查询语句解析器 已过时.
 * 1,将sql解析成片段.
 * 2,将各部分执行检查.
 * @author F.Fang
 *
 */
@Deprecated
public class SelectSqlParser extends SqlParser {
	
	/**
	 * 使用大写的形式处理Sql中的关键字并定位原始sql各重要位置.
	 * 为提高效率,避免产生过多字符串,设置为成员变量.
	 */
	private String upperSql;
	
	public SelectSqlParser(String sql){
		//执行非空检查和转义及某些初始化操作.
		super(sql);
		upperSql = sql.toUpperCase();
	}
	
	/**
	 * select 和from 之间是字段,字段之间是以','分割,Distinct可能会出现在字段之中.
	 * from 和where之间是表名.
	 * 
	 * where之后必然包含条件字段组合.
	 * 
	 * 末尾可能会存在 Order By Group By之类的操作符号.
	 * 
	 * 目前暂不检查关键字出现在字符串中的语句.
	 * eg:select id,name as 'from' from users;
	 * 即不支持sql中出现重复的关键字 
	 * 尽管语句在sql执行没有问题,但此类语句检查实际会发生错误.
	 * 
	 * 1) 验证原sql
	 * 2) 分离执行sql
	 * 3) 构造执行条件
	 * 
	 * 
	 * 以下关键字必须出现只允许一次.
	 * select, from
	 * 以下关键字可选 但至多出现一次.
     * where,distinct,order by,group by,limit
     * 
     * 规定：
     * 1,group by 后的字段一定要都出现在select fields当中.
     * 2,order by 后的字段也一定要出现在 select fields当中
     *   对于多库合并而言,Order by如果参与数据库查询,会造成结果集合不正确.
     *   如果将某个order by字段不在 select fields当中,那么该字段对结果集几乎没有意义.
     *   //目前这两种几乎没有检查.
	 * 
	 */
	@Override
	public SqlTree parse() {
		// (2) 执行* 或者.* 检查
		if (sql.contains(SeperatorSymbol.STAR) || sql.contains(SeperatorSymbol.POINTSTAR)) {
			throw new NestedComponentException("sql语句不合法,不允许使用[.*]或[*] !");
		}
		// (3) 解析出关键字,字段名,表名,等信息.

		// 其实也需要 ‘’ 匹配,即不能出现单个‘ 或者 ’,遇到转义符号自动忽略.
		// 此处暂不实现.

		// 各种生成的条件.
		Condition.Builder builder = new Condition.Builder();

		// 定义语法树
		SqlTree sqlTree = new SqlTree();
		/**
		 * 检查select关键字.
		 */
		// a,select 允许大小写.
		// 找到select的位置,select必须出现在语句的非空字符首位.
		int selectStartIndex = checkExistAndReturnStartIndex(SqlType.SELECT.name());
		int selectEndIndex = selectStartIndex + 6;

		// 对select关键字做空检查.
		// select之前的字符串必须是空串,select单词之后必须有一个空格
		checkKeyWordsSelect(selectStartIndex, selectEndIndex);

		/**
		 * 检查from 关键字.
		 */
		// b,form,一条完整的sql语句必然会包含from
		// 找到from的位置.

		int fromStartIndex = checkExistAndReturnStartIndex(SqlKeyWords.FROM);
		int fromEndIndex = fromStartIndex + 4;
		// from的前后必须都存在一个空格,并且之后不能为空串.
		checkKeyWordsFrom(fromStartIndex, fromEndIndex);

		/**
		 * 检查distinct关键字.
		 */
		int distinctStartIndex = upperSql.indexOf(SqlKeyWords.DISTINCT, selectEndIndex);
		if (distinctStartIndex != -1) {
			int distinctEndIndex = distinctStartIndex + 8;
			//检查关键字distinct前后的空格
			checkKeyWordsBlank(distinctStartIndex, distinctEndIndex, SqlKeyWords.DISTINCT);
			
			//设置distinct
			sqlTree.setDistinct(true);
			builder.distinct();
			
			//处理选择字段,并分离聚合函数.
			scanAggregateFunction(builder, distinctEndIndex, fromStartIndex, sqlTree);
		}else{
			//处理选择字段,并分离聚合函数.
			scanAggregateFunction(builder, selectEndIndex, fromStartIndex, sqlTree);
		}
		
		// 为了获取表名,必须确定语句当中是否包含Where , Group By , Order By
		int tableNameEndIndex = -1;

		/**
		 * 检查where关键字.
		 */
		// where关键字,至多存在一个.
		int whereStartIndex = upperSql.indexOf(SqlKeyWords.WHERE);
		if (whereStartIndex != -1) {
			// 并且要分离where当中的条件---暂不实现.
			int whereEndIndex = whereStartIndex + 5;
			// 如果存在where 关键字 则需要检查 where 关键字的前后空格
			checkKeyWordsBlank(whereStartIndex, whereEndIndex, SqlKeyWords.WHERE);
			// 只要where存在,必然是表名之前的字段.
			tableNameEndIndex = whereStartIndex;
		}

		// 限定where语句在前,以下条件均在后.

		/**
		 * 检查Group By关键字.
		 */
		// Group By 不参与db查询
		int groupByStartIndex = upperSql.indexOf(SqlKeyWords.GROUP_BY);
		if (groupByStartIndex != -1) {
			//检查空格
			int groupByEndIndex = groupByStartIndex+8;
			checkKeyWordsBlank(groupByStartIndex, groupByEndIndex, SqlKeyWords.GROUP_BY);
			
			if(tableNameEndIndex ==-1){
				tableNameEndIndex = groupByStartIndex;
			}
		}

		/**
		 * 检查Order By关键字.
		 * order by 可以连接max函数,但作用效果和无聚合函数类似.
		 */
		// Order By 不参与db查询.
		int orderByStartIndex = upperSql.indexOf(SqlKeyWords.ORDER_BY);
		if (orderByStartIndex != -1) {
			int orderByEndIndex = orderByStartIndex + 8;
			checkKeyWordsBlank(orderByStartIndex, orderByEndIndex, SqlKeyWords.ORDER_BY);
			if(tableNameEndIndex ==-1){
				tableNameEndIndex = orderByStartIndex;
			}
		}

		/**
		 * 检查limit关键字 .
		 */
		// 不参与db查询,不论是对接哪一种数据库,都采用limit,因为它并不参与具体的数据库查询.

		int limitStartIndex = upperSql.indexOf(SqlKeyWords.LIMIT);
		if (limitStartIndex != -1) {
			int limitEndIndex = limitStartIndex + 5;
			checkKeyWordsBlank(limitStartIndex, limitEndIndex, SqlKeyWords.LIMIT);
			if (tableNameEndIndex == -1) {
				tableNameEndIndex = limitStartIndex;
			}
		}
		
		//如果tableNameEndIndex仍然为-1,即语句中不好where order by , group by ,limit等等.
		if (tableNameEndIndex == -1) {
			// 以sql结尾位置作为表名位置.
			tableNameEndIndex = upperSql.length();
		} 
		//设置表信息.
		sqlTree.setTableInfo(sql.substring(fromEndIndex, tableNameEndIndex));
		
		//关键字顺序表.
		int[] keyWordsOrder = new int[] { selectStartIndex, distinctStartIndex, fromStartIndex, whereStartIndex,
						groupByStartIndex, orderByStartIndex, limitStartIndex };
		//检查关键字顺序表,若不通过,则抛出异常!.
		if (!checkKeyWordsOrder(keyWordsOrder)) {
			throw new NestedComponentException(
							"select sql语句不合法,各关键字位置顺序不正确,默认顺序(select, distinct, from, where, group by, order by, limit)!");
		}
		
		//处理where 条件.
		if(whereStartIndex!=-1){
			int neighborIndex = getNeighborKeyWordsStartIndex(keyWordsOrder, 3);
			int whereEndIndex = whereStartIndex + 5;
			//分离查询条件.
			sqlTree.setJoinCondition(sql.substring(whereEndIndex, neighborIndex));
		}
		
		// 处理group by 具体的字段暂不验证 
		if (groupByStartIndex != -1) {
			int neighborIndex = getNeighborKeyWordsStartIndex(keyWordsOrder, 4);
			int groupByEndIndex = groupByStartIndex + 8;
			String groupByFields = sql.substring(groupByEndIndex, neighborIndex);
			sqlTree.setGroupByFields(groupByFields);
			String[] groupByFieldsArr = groupByFields.split(SeperatorSymbol.COMMA);
			for (String field : groupByFieldsArr) {
				// 字段不允许出现函数,具体数据库执行时若出现函数也将失败,同时去掉空格
				// 如果选择字段中不包含 group by字段,则不作为二次group by字段.
				// select id,max(password) from user group by id,name;
				// 1,各个db执行:select id,max(password) from user group by id,name;
				// 2,数据汇总时执行:select id,max(password) from user group by id; name字段不出现在选择字段中,对汇总没有影响.
			    String groupByField = field.trim();
				if(sqlTree.getSelectSqlKeys().contains(groupByField)){
					builder.groupBy(groupByField);
				}
			}
		}
		
		//处理 Order by 具体的字段暂不验证
		if (orderByStartIndex != -1) {
			int neighborIndex = getNeighborKeyWordsStartIndex(keyWordsOrder, 5);
			int orderByEndIndex = orderByStartIndex + 8;
			String orderByFields = sql.substring(orderByEndIndex, neighborIndex);
			String[] orderByFieldsArr = orderByFields.split(SeperatorSymbol.COMMA);
			for (String field : orderByFieldsArr) {
				String upperField = field.toUpperCase();
				if (upperField.contains(OrderByType.ASC.name())) {
					int ascIndex = upperField.indexOf(OrderByType.ASC.name());
					String orderByField = field.substring(0, ascIndex).trim();
					if(sqlTree.getSelectSqlKeys().contains(orderByField)){
						builder.orderBy(orderByField, OrderByType.ASC);
					}
				} else if (upperField.contains(OrderByType.DESC.name())) {
					int descIndex = upperField.indexOf(OrderByType.DESC.name());
					String orderByField = field.substring(0, descIndex).trim();
					if(sqlTree.getSelectSqlKeys().contains(orderByField)){
						builder.orderBy(orderByField, OrderByType.DESC);
					}
				} else{
					String orderByField = field.trim();
					if(sqlTree.getSelectSqlKeys().contains(orderByField)){
						builder.orderBy(orderByField, OrderByType.ASC);
					}
				}
			}

		}
		
		// 处理limit 分页
		if (limitStartIndex != -1) {
			int limitEndIndex = limitStartIndex + 5;
			// limit属于最后一个关键字.
			// 分页处理时也需要做验证.
			int neighborIndex = upperSql.length();
			String pageSettings = sql.substring(limitEndIndex, neighborIndex);
			String[] pageSettingsArr = pageSettings.split(SeperatorSymbol.COMMA);
			
			try{
				int startIndex = -1;
				int offSet = -2;
				if (pageSettingsArr.length == 2) {
					startIndex = Integer.parseInt(pageSettingsArr[0].trim());
					//offSet允许为-1
					offSet = Integer.parseInt(pageSettingsArr[1].trim());
				} else if(pageSettingsArr.length==1){
					startIndex = 0;
					offSet = Integer.parseInt(pageSettingsArr[0].trim());
				}
				if(startIndex<0 || offSet<-1){
					throw new NestedComponentException("sql语句不合法,limit关键字分页设置不正确,起始位置和偏移量大小不合法!");
				}
				builder.paging(startIndex, offSet);
			}catch(Exception e){
				//可能是数字转换错误.
				throw new NestedComponentException("sql语句不合法,limit关键字分页设置值有误,"+ e.getMessage());
			}
		}
		
		sqlTree.setOrignalSql(sql);
		sqlTree.setCondition(builder.build());
		
		return sqlTree;
	}
	
	/**
	 * 获取邻接关键字的位置.
	 * @param keyWordsOrder
	 * @param orderIndex
	 * @return
	 */
	private int getNeighborKeyWordsStartIndex(int[] keyWordsOrder, int orderIndex){
		int neighborIndex = -1;
		int len = keyWordsOrder.length;
		for(int i= orderIndex+1;i<len;++i){
			if(keyWordsOrder[i]!=-1){
				neighborIndex = keyWordsOrder[i];
				break;
			}
		}
		if(neighborIndex==-1){
			neighborIndex = upperSql.length();
		}
		return neighborIndex;
	}
	
	/**
	 * 检查关键字顺序,若语句中以下关键字均存在,则保证顺序为.
	 * select, distinct, from, where, group by, order by, limit.
	 */
	private boolean checkKeyWordsOrder(int[] keyWordsOrder) {
		// 预留在之后实现 要保证数组过滤掉数字为-1的值后,从左至右数字从小到大.
		Arrays.copyOf(keyWordsOrder, keyWordsOrder.length);

		// 原始的数组
		//int[] keyWordsOrder = new int[] { 5, 15, -1, 20, -1, -1, 25 };
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
		return sb1.toString().equals(sb2.toString());
	}
	
	/**
	 * 在实际的各字段匹配中,如果包含聚合函数,实际上还需要检查 "(" ")"的匹配问题.
	 * @param builder
	 * @param startIndex
	 * @param endIndex
	 * @param sqlTree
	 */
	private void scanAggregateFunction(Condition.Builder builder, int startIndex, int endIndex, SqlTree sqlTree) {
		String selectFields = sql.substring(startIndex, endIndex);
		if(StringUtils.isBlank(selectFields)){
			throw new NestedComponentException("select sql语句不合法,选择字段不允许为空!");
		}

		List<String> selectKeys = new ArrayList<String>();
		
		StringBuffer selectFieldsBuffer = new StringBuffer();
		String[] fields = selectFields.split(SeperatorSymbol.COMMA);
		for(String field: fields){
			String formatField = null;
			for (AggregationType type : AggregationType.values()) {
				String typeName = type.name();
				String upperField = field.toUpperCase();
				int typeStartIndex = upperField.indexOf(typeName+SeperatorSymbol.LEFT_PARENTHESIS);
				if (typeStartIndex != -1) {
					int left = upperField.indexOf(SeperatorSymbol.LEFT_PARENTHESIS, typeStartIndex);
					int right = upperField.indexOf(SeperatorSymbol.RIGHT_PARENTHESIS, typeStartIndex);
					String fieldName = field.substring(left+1, right).trim();
					//新增聚合函数条件
					addAggregateFunction(builder, fieldName, type);
					selectKeys.add(fieldName);
					
					String oldAggregate = field.substring(typeStartIndex, typeStartIndex+typeName.length());
					formatField = field.replaceFirst(oldAggregate, typeName).trim();
					//聚合函数不可能重叠出现,因此搜索成功一次后就完成了.
					break;
				}
			}
			if(formatField ==null){
				formatField = field.trim();
				selectKeys.add(formatField);
			}
			selectFieldsBuffer.append(formatField+SeperatorSymbol.COMMA);
		}
		sqlTree.setSelectSqlKeys(selectKeys);
		
		if(fields.length>0){
			selectFieldsBuffer.deleteCharAt(selectFieldsBuffer.length()-1);
		}

		sqlTree.setSelectFields(selectFieldsBuffer.toString());
	}
	
	/**
	 * 填充聚合函数.
	 * @param builder
	 * @param fieldName
	 * @param type
	 */
	private void addAggregateFunction(Condition.Builder builder, String fieldName, AggregationType type) {
		switch (type) {
		case COUNT:
			builder.count(fieldName);
			break;
		case MAX:
			builder.max(fieldName);
			break;
		case MIN:
			builder.min(fieldName);
			break;
		case SUM:
			builder.sum(fieldName);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 检查关键字前后的空格.
	 * @param startIndex
	 * @param endIndex
	 * @param key
	 */
	private void checkKeyWordsBlank(int startIndex, int endIndex, String key){
		// 验证关键字之前的空格
		if (upperSql.charAt(startIndex - 1) != SeperatorSymbol.EMPTY.charAt(0)) {
			throw new NestedComponentException("select sql语句不合法,"+key+"关键字之前必须是一个空格!");
		}
		// 验证关键字之后的空格
		if (upperSql.charAt(endIndex) != SeperatorSymbol.EMPTY.charAt(0)) {
			throw new NestedComponentException("select sql语句不合法,"+key+"关键字之后必须紧跟一个空格!");
		}
	}

	/**
	 * 检查select关键字.
	 * 
	 * @param startIndex
	 *            .
	 * @param endIndex
	 *            .
	 */
	private void checkKeyWordsSelect(int startIndex, int endIndex) {
		// select之前的字符串必须为空
		if (StringUtils.isNotBlank(upperSql.substring(0, startIndex))) {
			throw new NestedComponentException("select sql语句不合法,select关键字之前必须不存在空串!");
		}

		//如果语句不完整.
		if(endIndex>=upperSql.length()){
			throw new NestedComponentException("select sql语句不合法,sql语句不完整!");
		}
		
		// 从select单词之后必须有一个空格
		if (upperSql.charAt(endIndex) != SeperatorSymbol.EMPTY.charAt(0)) {
			throw new NestedComponentException("select sql语句不合法,select关键字之后必须紧跟一个空格!");
		}
	}

	/**
	 * 检查from关键字.
	 * 
	 * @param startIndex
	 * @param endIndex
	 */
	private void checkKeyWordsFrom(int startIndex, int endIndex) {
		// 检查from之前的空格
		if (upperSql.charAt(startIndex - 1) != SeperatorSymbol.EMPTY.charAt(0)) {
			throw new NestedComponentException("select sql语句不合法,from关键字之前必须是一个空格!");
		}

		// 第一次检查表名是否存在,只是用来判断sql语句是否为.
		String afterFromEnd = upperSql.substring(endIndex);
		if (StringUtils.isBlank(afterFromEnd)) {
			throw new NestedComponentException("sql语句 from关键字之后必须有表名!");
		}

		// 检查from之后的空格
		if (upperSql.charAt(endIndex) != SeperatorSymbol.EMPTY.charAt(0)) {
			throw new NestedComponentException("select sql语句不合法,from关键字之后必须紧跟一个空格!");
		}
	}

	/**
	 * 检查关键字的存在.
	 * 
	 * @param key
	 *            关键字.
	 */
	private int checkExistAndReturnStartIndex(String key) {
		int startIndex = upperSql.indexOf(key);
		if (startIndex == -1) {
			throw new NestedComponentException("select sql语句不合法,必须包含不区分大小写的完整格式" + key + "关键字!");
		}
		return startIndex;
	}

	
//	private void checkDuplicate() {
//		String select = SqlType.SELECT.name();
//
//		// 如果select存在那么只允许一次
//		if (upperSql.indexOf(select) != upperSql.lastIndexOf(select)) {
//			throw new MenloException("select sql语句不合法,关键字select字符串只能出现一次!");
//		}
//
//		// 如果from存在那么只允许一次
//		if (upperSql.indexOf(SqlKeyWords.FROM) != upperSql.lastIndexOf(SqlKeyWords.FROM)) {
//			throw new MenloException("select sql语句不合法,关键字select字符串只能出现一次!");
//		}
//
//		// Where只能出现一次.
//		if (upperSql.indexOf(SqlKeyWords.WHERE) != upperSql.lastIndexOf(SqlKeyWords.WHERE)) {
//			throw new MenloException("select sql语句不合法,关键字Where字符串只能出现一次!");
//		}
//
//		// Group By只能出现一次.
//		if (upperSql.indexOf(SqlKeyWords.GROUP_BY) != upperSql.lastIndexOf(SqlKeyWords.GROUP_BY)) {
//			throw new MenloException("select sql语句不合法,关键字Group By字符串只能出现一次!");
//		}
//
//		// Order By只能出现一次.
//		if (upperSql.indexOf(SqlKeyWords.ORDER_BY) != upperSql.lastIndexOf(SqlKeyWords.ORDER_BY)) {
//			throw new MenloException("select sql语句不合法,关键字Order By字符串只能出现一次!");
//		}
//	}

}