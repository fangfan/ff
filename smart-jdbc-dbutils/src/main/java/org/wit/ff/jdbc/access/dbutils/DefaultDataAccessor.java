package org.wit.ff.jdbc.access.dbutils;

import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by F.Fang on 2015/3/31.
 * 默认以配置数据源的方式获取连接.
 * Version :2015/3/31
 */
public class DefaultDataAccessor extends AbstractDataAccessor {

    private DataSource dataSource;

    protected Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }

    @Override
    protected void closeConn(Connection conn) {
        try {
            DbUtils.close(conn);
        } catch (SQLException e) {
            // do nothing!
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
