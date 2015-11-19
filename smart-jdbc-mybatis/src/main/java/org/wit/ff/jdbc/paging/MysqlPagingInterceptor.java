package org.wit.ff.jdbc.paging;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.wit.ff.jdbc.dialect.Dialect;
import org.wit.ff.jdbc.dialect.db.MySQLDialect;

import java.sql.Connection;

/**
 * Created by F.Fang on 2015/11/19.
 */
@Intercepts(
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})
)
public class MysqlPagingInterceptor extends PagingInterceptor{
    private Dialect dialect = new MySQLDialect();

    @Override
    public Dialect getDialect() {
        return dialect;
    }
}
