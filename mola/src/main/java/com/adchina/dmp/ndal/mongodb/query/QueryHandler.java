package com.adchina.dmp.ndal.mongodb.query;

import com.adchina.dmp.ndal.IQuery;
import com.adchina.dmp.ndal.mongodb.Constants;
import com.adchina.dmp.ndal.mongodb.MongoUtils;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by F.Fang on 2015/3/2.
 * Version :2015/3/2
 */
public class QueryHandler<T> {

    public Collection<T> execute(IQuery query, DB db) {
        if (query instanceof AggregationQuery) {
            return executeBasicQuery((AggregationQuery) query, db);
        } else if (query instanceof BasicQuery) {
            return executeBasicQuery((BasicQuery) query, db);
        }
        return new ArrayList<>();
    }

    private Collection<T> executeBasicQuery(AggregationQuery aggregationQuery, DB db) {

        DBCollection collection = db.getCollection(aggregationQuery.getCollection());

        List<DBObject> list = new ArrayList<>();
        if (aggregationQuery.getAggregationOptions() == null) {
            AggregationOutput output = collection.aggregate(aggregationQuery.getPipeline());
            for(DBObject obj :output.results()){
                translateGroupFields(obj);
                list.add(obj);
            }
        } else {
            Cursor cursor = collection.aggregate(aggregationQuery.getPipeline(), aggregationQuery.getAggregationOptions());
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                translateGroupFields(obj);
                list.add(obj);
            }
        }
        return MongoUtils.build(aggregationQuery.getResultType(), list);
    }

    private void translateGroupFields(DBObject obj){
        if(obj.containsField(Constants._ID)){
            Object id = obj.get(Constants._ID);
            if(id instanceof DBObject){
                DBObject idObj = (DBObject)id;
                Set<String> keys = ((DBObject)id).keySet();
                for(String key: keys){
                    obj.put(key,idObj.get(key));
                }
                // remove _id
                obj.removeField(Constants._ID);
            }
        }
    }

    private Collection<T> executeBasicQuery(BasicQuery basicQuery, DB db) {
        Collection<T> result = new ArrayList<T>();
        DBCollection collection = db.getCollection(basicQuery.getCollection());
        DBCursor cursor = null;
        try {
            DBObject where = basicQuery.getWhere();
            if (where != null) {
                if (basicQuery.getSelect() != null) {
                    cursor = collection.find(where, basicQuery.getSelect());
                } else {
                    cursor = collection.find(where);
                }
            } else {
                if (basicQuery.getSelect() != null) {
                    cursor = collection.find(null, basicQuery.getSelect());
                } else {
                    cursor = collection.find();
                }
            }

            if (basicQuery.getSort() != null) {
                cursor.sort(basicQuery.getSort());
            }

            if (basicQuery.isPage()) {
                if (basicQuery.getSkip() == 0) {
                    cursor.limit(basicQuery.getLimit());
                } else {
                    cursor.skip(basicQuery.getSkip()).limit(basicQuery.getLimit());
                }
            }
            result = MongoUtils.build(basicQuery.getResultType(), cursor.toArray());
        } finally {
            // 默认不关闭cursor.
//            if (cursor != null) {
//                cursor.close();
//            }
        }
        return result;
    }
}
