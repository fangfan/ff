package org.wit.fddl.sqlparser;

import org.apache.commons.lang3.StringUtils;

import org.wit.fddl.exception.NestedComponentException;
import org.wit.fddl.sql.SqlTree;

/**
 * Sql解析器.
 * @author F.Fang
 *
 */
public abstract class SqlParser {
	
	/**
	 * 原始sql.
	 */
	protected String sql;
	
	/**
	 * 构造函数.
	 * @param sql
	 */
	public SqlParser(String sql){
		this.sql = initSql(sql);
	}
	
	/**
	 * 通过用户输入的sql语句生成SqlTree.
	 * 即生成一些sql片段信息.
	 * @return
	 */
	protected abstract SqlTree parse();
	
	/**
	 * 后续可能还会有一些对原始sql做格式化的操作
	 * 比如去掉注释...
	 * @param sql
	 * @return
	 */
	private String initSql(String sql) {
		// 执行非空检查
		if (StringUtils.isBlank(sql)) {
			throw new NestedComponentException("sql语句不允许为空!");
		}
		//去除换行符号
		String lineSeparator = System.getProperty("line.separator");
		String oneLineSql = sql.replaceAll(lineSeparator, "");
		//执行转义,为避免 ‘ “ 通过转义产生的差别,将 \"均转换为 '
		String escSql = oneLineSql.replaceAll("\"", "'");
		return escSql;
	}

}
