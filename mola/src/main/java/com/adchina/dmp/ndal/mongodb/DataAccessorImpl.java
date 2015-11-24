package com.adchina.dmp.ndal.mongodb;

import com.adchina.dmp.ndal.IDataAccessor;
import com.adchina.dmp.ndal.IQuery;
import com.adchina.dmp.ndal.IUpdater;
import com.adchina.dmp.ndal.mongodb.query.QueryHandler;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.util.Collection;

/**
 * Created by F.Fang on 2015/3/2.
 * Version :2015/3/2
 */
public class DataAccessorImpl implements IDataAccessor {

    private MongoClient client;

    private String dbName;

    @Override
    public <T> Collection<T> query(IQuery q) {
        if (q != null) {
            DB db = client.getDB(dbName);
            return new QueryHandler<T>().execute(q,db);
        }
        return null;
    }

    @Override
    public void update(IUpdater u) {

    }

    @Override
    public void insert(IUpdater i) {

    }

    @Override
    public void delete(IUpdater d) {

    }

    public void setClient(MongoClient client) {
        this.client = client;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
