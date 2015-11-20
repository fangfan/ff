package org.wit.ff.jdbc.sql.db;

import org.wit.ff.jdbc.dialect.Dialect;
import org.wit.ff.jdbc.dialect.db.OracleDialect;
import org.wit.ff.jdbc.sql.SQLBuilder;

/**
 * Created by F.Fang on 2015/11/20.
 */
public class OracleBuilder extends SQLBuilder {

    private Dialect dialect = new OracleDialect();

    @Override
    public Dialect getDialect() {
        return dialect;
    }
}
