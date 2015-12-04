package org.wit.fddl.sqlparser;

public interface RegexConstants {
	/**
	 * 基础选择语句匹配格式.
	 */
	String SELECT_BASE = "(select)(.+)(from)";
	
	/**
	 * 表名匹配格式.
	 */
	String SELECT_TABLEINFO = "(from)(.+)(where|group by|order by|limit)";
	
	/**
	 * 包含where子句匹配格式.
	 */
	String SELECT_WHERE = "(from)(.+)(where)";
	
	/**
	 * 包含Group by子句匹配格式.
	 */
	String SELECT_GROUP_BY = "(from)(.+)(group by)";
	
	/**
	 * 包含Order by子句匹配格式.
	 */
	String SELECT_ORDER_BY = "(from)(.+)(order by)";
	
	/**
	 * 包含limit子句匹配格式.
	 */
	String SELECT_LIMIT = "(from)(.+)(limit)";

}
