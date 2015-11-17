package org.wit.ff.jdbc.access.dbutils;

import org.slf4j.Logger;
import org.wit.ff.jdbc.exception.DbUtilsDataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by F.Fang on 2015/4/23.
 * Version :2015/4/23
 */
public class SingleConnDataAccessor extends AbstractDataAccessor {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(SingleConnDataAccessor.class);
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    private String driver;
    private String username;
    private String password;
    private String url;

    private SingleConnDataAccessor(String driver, String username, String password, String url){
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public static SingleConnDataAccessor getInstance(String driver, String username, String password, String url){
        return new SingleConnDataAccessor(driver,username,password,url);
    }

    protected Connection getConnection() throws SQLException {
        if(connectionHolder.get()==null){
            Connection conn = initConnection();
            connectionHolder.set(conn);
        }
        return connectionHolder.get();
        // 虽然可以将连接绑定到事务,但是当外部循dataAccessor的方法时,环调用时会产生多个连接.
        //return DataSourceUtils.getConnection(dataSource);
        // 以下写法无法解决事务问题,无法将连接绑定到Spring 事务.
        // return dataSource.getConnection();
    }

    @Override
    protected void closeConn(Connection conn) {
        // do nothing!
    }


    /**
     * synchronized get database connection.
     *
     * @return
     */
    private synchronized Connection initConnection() {
        LOG.info("getConnection, driver=" + driver + ",username=" + username + ",password=" + password + ",url=" + url);
        if (connectionHolder.get() == null) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                LOG.error("driver not found", e);
                throw new DbUtilsDataAccessException("driver not found!", e);
            }
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                connectionHolder.set(connection);
            } catch (SQLException e) {
                LOG.error("get connection error!", e);
                throw new DbUtilsDataAccessException("get connection error!", e);
            }
        }
        return connectionHolder.get();
    }

    public synchronized void releaseConnection() {
        if (connectionHolder.get() != null) {
            try {
                connectionHolder.get().close();
                connectionHolder.remove();
            } catch (SQLException e) {
                LOG.error("release connection error!", e);
            }
        }
    }

}
