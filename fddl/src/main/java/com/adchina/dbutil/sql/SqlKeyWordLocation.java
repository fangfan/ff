package com.adchina.dbutil.sql;

/**
 * 关键字在Sql语句中的位置.
 * 
 * @author F.Fang
 *
 */
public class SqlKeyWordLocation {
	private String keyword;
	
	private int start = -1;
	
	private int end = -1;
	
	public SqlKeyWordLocation(String keyword, int start, int end){
		this.keyword = keyword;
		this.start = start;
		this.end = end;
	}
	
	public SqlKeyWordLocation(String keyword){
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

}
