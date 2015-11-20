package org.wit.ff.jdbc.sql.db;


import org.wit.ff.jdbc.dialect.Dialect;
import org.wit.ff.jdbc.dialect.db.MySQLDialect;
import org.wit.ff.jdbc.sql.SQLBuilder;

/**
 * Created by F.Fang on 2015/2/16.
 * Version :2015/2/16
 */
public class MySQLBuilder extends SQLBuilder {

    private Dialect dialect = new MySQLDialect();

    @Override
    public Dialect getDialect() {
        return dialect;
    }
}
