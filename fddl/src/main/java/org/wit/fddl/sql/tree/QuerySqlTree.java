package org.wit.fddl.sql.tree;

import java.util.List;

import org.wit.fddl.condition.Condition;
import org.wit.fddl.sql.SeperatorSymbol;
import org.wit.fddl.sql.SqlKeyWords;
import org.wit.fddl.sql.SqlType;
import org.wit.fddl.sql.TableInfo;
import org.wit.fddl.sql.keys.SelectSqlKey;

public class QuerySqlTree extends AbstractSqlTree {

    public QuerySqlTree(String sql) {
        super(sql);
    }

    /**
     * 原始语句.
     */
    private String orignalSql;

    /**
     * 去重复.
     */
    private boolean distinct;

    /**
     * 选择字段.
     */
    private String selectFields;
    
    /**
     * 选择字段.
     */
    private List<SelectSqlKey> selectSqlKeys;

    /**
     * 表相关信息.
     */
    private String tableInfo;
    
    /**
     * 表相关信息.
     */
    private List<TableInfo> tablesInfo;

    /**
     * 连接条件.
     * 因为涉及连接符号的问题,暂时不做过多验证.
     */
    private String joinCondition;

    /**
     * Group By语句.
     */
    private String groupByFields;

    /**
     * Order By语句.
     */
    private String orderByFields;

    /**
     * 合并条件.
     */
    private Condition condition;

    /**
     * 组织db执行sql.
     * 
     * @return
     */
    public String getDbExecuteSql() {
        StringBuffer sb = new StringBuffer();
        sb.append(SqlType.SELECT.name());
        sb.append(SeperatorSymbol.EMPTY);
        for(int i=0,len=selectSqlKeys.size();i<len;++i){
            SelectSqlKey selectKey = selectSqlKeys.get(i);
            sb.append(selectKey.getSqlSegment());
            if(i!=(len-1)){
                sb.append(SeperatorSymbol.COMMA);
            }
        }
        sb.append(SeperatorSymbol.EMPTY);
        sb.append(SqlKeyWords.FROM);
        sb.append(tableInfo);
        if (joinCondition != null) {
            sb.append(SqlKeyWords.WHERE);
            sb.append(joinCondition);
        }
        if (groupByFields != null) {
            sb.append(SeperatorSymbol.EMPTY);
            sb.append(SqlKeyWords.GROUP_BY);
            sb.append(SeperatorSymbol.EMPTY);
            sb.append(groupByFields);
            sb.append(SeperatorSymbol.EMPTY);
        }

        if (orderByFields != null) {
            sb.append(SeperatorSymbol.EMPTY);
            sb.append(SqlKeyWords.ORDER_BY);
            sb.append(SeperatorSymbol.EMPTY);
            sb.append(orderByFields);
            sb.append(SeperatorSymbol.EMPTY);
        }

        if (condition != null && condition.isPagingFlag()) {
            sb.append(SqlKeyWords.LIMIT);
            sb.append(SeperatorSymbol.EMPTY);
            sb.append(condition.getPagingSettings()[1]);
        }

        return sb.toString();
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public String getSelectFields() {
        return selectFields;
    }

    public void setSelectFields(String selectFields) {
        this.selectFields = selectFields;
    }

    public String getJoinCondition() {
        return joinCondition;
    }

    public void setJoinCondition(String joinCondition) {
        this.joinCondition = joinCondition;
    }

    public String getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(String tableInfo) {
        this.tableInfo = tableInfo;
    }

    public String getOrignalSql() {
        return orignalSql;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setOrignalSql(String orignalSql) {
        this.orignalSql = orignalSql;
    }

    public String getGroupByFields() {
        return groupByFields;
    }

    public void setGroupByFields(String groupByFields) {
        this.groupByFields = groupByFields;
    }

    public String getOrderByFields() {
        return orderByFields;
    }

    public void setOrderByFields(String orderByFields) {
        this.orderByFields = orderByFields;
    }

    public List<TableInfo> getTablesInfo() {
        return tablesInfo;
    }

    public void setTablesInfo(List<TableInfo> tablesInfo) {
        this.tablesInfo = tablesInfo;
    }

    public List<SelectSqlKey> getSelectSqlKeys() {
        return selectSqlKeys;
    }

    public void setSelectSqlKeys(List<SelectSqlKey> selectSqlKeys) {
        this.selectSqlKeys = selectSqlKeys;
    }
}
