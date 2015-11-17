package org.wit.ff.jdbc.access.dbutils;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by F.Fang on 2015/4/23.
 * Version :2015/4/23
 */
public class DefaultTransactionDataAccessor extends AbstractDataAccessor {

    private DataSource dataSource;

    protected Connection getConnection() throws SQLException {

        return DataSourceUtils.getConnection(dataSource);
        // 虽然可以将连接绑定到事务,但是当外部循dataAccessor的方法时,环调用时会产生多个连接.
        //return DataSourceUtils.getConnection(dataSource);
        // 以下写法无法解决事务问题,无法将连接绑定到Spring 事务.
        // return dataSource.getConnection();
    }

    @Override
    protected void closeConn(Connection conn) {
        // do nothing!
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
