package com.adchina.dbutil.sql;

import java.util.List;

import com.adchina.dbutil.condition.Condition;

/**
 * Sql语句树.
 * @author F.Fang
 *
 */
public class SqlTree {
	
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
	 * 选择字段,为方便处理,生成字段列表.
	 */
	private List<String> selectSqlKeys;
	
	/**
	 * 表相关信息.
	 */
	private String tableInfo;
	
	/**
	 * 连接条件.
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
	 * @return
	 */
	public String getDbExecuteSql() {
		StringBuffer sb = new StringBuffer();
		sb.append(SqlType.SELECT.name());
		sb.append(SeperatorSymbol.EMPTY);
		sb.append(selectFields);
		sb.append(SeperatorSymbol.EMPTY);
		sb.append(SqlKeyWords.FROM);
		sb.append(tableInfo);
		if (joinCondition != null) {
			sb.append(SqlKeyWords.WHERE);
			sb.append(joinCondition);
		}
		if (groupByFields != null) {
			sb.append(SqlKeyWords.GROUP_BY);
			sb.append(groupByFields);
		}
		
		if(orderByFields!=null){
		    sb.append(SqlKeyWords.ORDER_BY);
		    sb.append(orderByFields);
		}
		
		if(condition!=null&& condition.isPagingFlag()){
		    sb.append(SqlKeyWords.LIMIT);
		    sb.append(SeperatorSymbol.EMPTY);
            sb.append(condition.getPagingSettings()[1]);
		}
		
		return sb.toString();
	}

	public List<String> getSelectSqlKeys() {
		return selectSqlKeys;
	}

	public void setSelectSqlKeys(List<String> selectSqlKeys) {
		this.selectSqlKeys = selectSqlKeys;
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
	
}
