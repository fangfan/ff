package org.wit.ff.jdbc.sql.mysql;


import org.wit.ff.jdbc.sql.SQLBuilder;

/**
 * Created by F.Fang on 2015/2/16.
 * Version :2015/2/16
 */
public class MySQLBuilder extends SQLBuilder {

    private boolean paging;
    private int offset;
    private int pageSize;

    @Override
    public SQLBuilder PAGE(int offset, int pageSize) {
        paging = true;
        // 这里作为底层接口不做任何检查,上层业务调用时检查.
        this.offset = offset;
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public String toString() {
        if(paging){
            return super.toString()+" limit "+offset+","+pageSize;
        }else{
            return super.toString();
        }
    }
}
