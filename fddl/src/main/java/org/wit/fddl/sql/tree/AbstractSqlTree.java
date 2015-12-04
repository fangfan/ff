package org.wit.fddl.sql.tree;

public abstract class AbstractSqlTree {
    
    /**
     * 原始sql.
     */
    protected String sql;
    
    /**
     * 构造函数.
     * @param sql
     */
    public AbstractSqlTree(String sql){
        this.sql = sql;
    }
    
    public abstract String getDbExecuteSql();
    

}
