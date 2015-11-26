package com.adchina.dbutil.sql.parser;

import org.junit.Test;

import com.adchina.dbutil.sql.tree.QuerySqlTree;

public class QuerySqlParserTest {
    
    @Test
    public void demo(){
        String sql = "select id,max(username) from users where id=? group by id order by id,password desc limit 0,1";
        //sql = "select u.id, max(username) as maxName, min(password) as minName from users u where id=? group by u.id order by u.id desc limit 0,1";
        sql = "select id,min(password) as mpwd from users group by id,username order by mpwd desc";
        
        QuerySqlParser sqlParser = new QuerySqlParser(sql);
        QuerySqlTree sqlTree = (QuerySqlTree) sqlParser.parse();
        System.out.println(sqlTree.getDbExecuteSql());
    }

}
