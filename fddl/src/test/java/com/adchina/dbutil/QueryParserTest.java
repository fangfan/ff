package com.adchina.dbutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.adchina.dbutil.sql.SqlTree;
import com.adchina.dbutil.sqlparser.QuerySqlParser;

public class QueryParserTest {

	@Test
	public void testParse(){
		String sql ="select id,max(username),min(password) from users where id=? group by id order by id desc limit 0,1";
//		sql = "select";//no
//		sql = "select id from users";
//		sql = "select id from users where id=?";
//		sql = "select id from users where id=? group by id";
		SqlTree tree= new  QuerySqlParser(sql).parse();
		System.out.println(tree.getDbExecuteSql());
	}
	
	@Test
	public void testFindBestMatchKeyWords(){
		String sql = " SELECT id as 'id  from ',name From users where id='1' ;";
		System.out.println(sql);
		String regex="(select )(.+)( from)";
		String regexFrom = " from ";
	    String cols=getMatchedStringOnce(regex,sql);
	    System.out.println(cols);
	    System.out.println(isContains(sql,regexFrom));
		
	}
	
	@Test
	public void testMatch(){
		String sql1 = "SELECT id as 'idfrom',name From users where id='1'";
		String regex1="(select )(.+)( from)";
		String cols1=getMatchedString(regex1,sql1);
		assertEquals("id as 'idfrom',name",cols1);
		
		String sql2 = "SELECT id as 'idfrom',name";
		String cols2=getMatchedString(regex1,sql2);
		assertNull(cols2);
		
		//先以select from格式匹配
		String sql3 = "select id,max(username),min(password) from users where id=? group by id order by id desc limit 0,1";
		//再匹配"(from)(.+)( where | groups+by | orders+by)
		
		String regex3 = "(from )(.+)( where | groups by + | orders by +)";
		String cols3=getMatchedString(regex3,sql3);
		assertEquals("users",cols3);
		
		String sql4 = "select id from users where id=?";
		String regex4 = "(from )(.+)( where | groups by + | orders by +)";
		String cols4=getMatchedStringOnce(regex4,sql4);
		
		assertEquals("users",cols4);
	}
	
	@Test
	public void testFullMatch() {
		// 先以select from格式匹配
		String sql = "select id,max(username),min(password) from users where id=? group by id order by id desc limit 0,1";
		System.out.println(sql.length());
		String regex = "(select )(.+)(from )(.+)( where | group by | order by |limit )";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);
		if(matcher.find()){
			int groupCount = matcher.groupCount();
			System.out.println(groupCount);
			for(int i=1;i<=groupCount;++i){
				System.out.println(matcher.group(i));
			}
			
			System.out.println(matcher.end());
		}
	}
	
	@Test
	public void testBaseSqlMatch(){
		String baseSql = "iselect id as 'from',name from users";
		String regex = "(select)(.+)(from)";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(baseSql);
		if(matcher.find()){
			//处理选择字段.
			System.out.println(baseSql.substring(matcher.end()));
			System.out.println(matcher.group(2));
		}else{
			//Sql语句异常.
			System.out.println("no");
		}
	}
	
	@Test
	public void testTableNameSqlMatch(){
		//检查是否存在 group by order by limit等字段.
		String tableNameSql = "select id,name from users";
		String tableNameRegex = "(from )(.+)( where | group by | order by |limit )";
		Pattern pattern = Pattern.compile(tableNameRegex, Pattern.CASE_INSENSITIVE);
		Matcher tableNameMatcher = pattern.matcher(tableNameSql);
		if(tableNameMatcher.find()){
			System.out.println("first match!");
			int groupCount = tableNameMatcher.groupCount();
			for(int i=1;i<=groupCount;++i){
				System.out.println(tableNameMatcher.group(i));
			}
		}else{
			//继续处理表名.
			String tableNameRegex1 = "(from )(.+)";
			Pattern pattern1 = Pattern.compile(tableNameRegex1, Pattern.CASE_INSENSITIVE);
			Matcher tableNameMatcher1 = pattern1.matcher(tableNameSql);
			if(tableNameMatcher1.find()){
				System.out.println("second match!");
				System.out.println(tableNameMatcher1.group(2));
			}else{
				System.out.println("no");
			}
		}
	}
	
	@Test
	public void testWhereSqlMatch(){
		//检查是否存在where等字段.
		String whereSql = "select id,name from users Where id=? group by id";
		String whereRegex = "(from)(.+)(where )";
		Pattern pattern = Pattern.compile(whereRegex, Pattern.CASE_INSENSITIVE);
		Matcher whereMatcher = pattern.matcher(whereSql);
		if(whereMatcher.find()){//存在where子句.
			System.out.println(whereMatcher.end());
			System.out.println(whereSql.substring(whereMatcher.end()));
		}else{
			System.out.println("no");
		}
	}
	
	@Test
	public void testNewLineSqlMatch(){
		String baseSql = "iselect id "+System.getProperty("line.separator")+"as 'from',name from users";
		System.out.println(baseSql);
		baseSql = baseSql.replaceAll(System.getProperty("line.separator"), "");
		System.out.println(baseSql);
		String regex = "(select)(.+)(from)";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(baseSql);
		if(matcher.find()){
			//处理选择字段.
			System.out.println(baseSql.substring(matcher.end()));
			System.out.println(matcher.group(2));
		}else{
			//Sql语句异常.
			System.out.println("no");
		}
	}
	
	@Test
	public void testLimitMatch(){
		String whereSql ="select id,max(username),min(password) from users where id=? group by id order by id desc limit 0,1";
		//String whereSql = "select id,name from users Where id=? group by id limit 0,1";
		String whereRegex = "(from)(.+)(limit)";
		Pattern pattern = Pattern.compile(whereRegex, Pattern.CASE_INSENSITIVE);
		Matcher whereMatcher = pattern.matcher(whereSql);
		if(whereMatcher.find()){//存在where子句.
			System.out.println(whereMatcher.end());
			System.out.println(whereSql.substring(whereMatcher.end()));
		}else{
			System.out.println("no");
		}
	}
	
    private static int isContains(String lineText,String word){
        Pattern pattern=Pattern.compile(word,Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(lineText);
        matcher.find();
        //System.out.println(matcher.start());
        return matcher.start();
    }
    
    private static String getMatchedStringOnce(String regex,String text){
        Pattern pattern=Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        //Pattern pattern=Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(text);
        if(matcher.find()){
//        	System.out.println(matcher.start());
//        	System.out.println(matcher.end());
            return matcher.group(2);
        }
        return null;
    }
	
	private static String getMatchedString(String regex,String text){
        Pattern pattern=Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        //Pattern pattern=Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(text);
        while(matcher.find()){
//        	System.out.println(matcher.start());
//        	System.out.println(matcher.end());
            return matcher.group(2);
        }
        return null;
    }

}
