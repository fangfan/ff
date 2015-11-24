package com.adchina.dmp.ndal;

import com.adchina.dmp.ndal.model.User;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by F.Fang on 2015/2/26.
 * Version :2015/2/26
 */
public class MongoTest {

    @Test
    public void trans() {
        BasicDBObject obj = new BasicDBObject("name", "himongo")
                .append("age", 18)
                .append("gender", 1)
                .append("address", new BasicDBObject("country", "china").append("city", "shanghai"));

        String json = JSON.serialize(obj);
        System.out.println(json);
        User user = JsonUtil.jsonToObject(json, User.class);

        //Object result = JSON.parse(json);
        System.out.println(user);
    }

    /**
     * insert可以通过T->json->DBObject.
     */
    @Test
    public void insert() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testCollection");
            BasicDBObject obj = new BasicDBObject("name", "himongo")
                    .append("age", 18)
                    .append("gender", 1)
                    .append("address", new BasicDBObject("country", "china").append("city", "shanghai"));
            dbCollection.insert(obj);

            // 相当于关系型数据库的主键.
            System.out.println(obj.getObjectId("_id"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void insertListAttr() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testAttrList");

            BasicDBList dbList = new BasicDBList();
            dbList.add(new BasicDBObject("country", "china").append("city", "上海"));
            dbList.add(new BasicDBObject("country", "china").append("city", "beijing"));

            BasicDBObject obj = new BasicDBObject("name", "中文测试")
                    .append("age", 18)
                    .append("gender", 1)
                    .append("address", dbList);

            dbCollection.insert(obj);

            // 相当于关系型数据库的主键.
            System.out.println(obj.getObjectId("_id"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void findListAtrr() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testAttrList");

            BasicDBObject query = new BasicDBObject("address.0.country", "china");

            DBCursor cusor = dbCollection.find(query);
            while(cusor.hasNext()){
                Object obj = cusor.next();
                System.out.println(obj);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally

        {
            if (client != null) {
                client.close();
            }
        }
    }


    @Test
    public void testDate() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testDate");
            BasicDBObject obj = new BasicDBObject("name", "himongo")
                    .append("age", 18)
                    .append("gender", 1)
                    .append("date", new Date());
            dbCollection.insert(obj);
            // 相当于关系型数据库的主键.
            System.out.println(obj.getObjectId("_id"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void testQueryDate() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testDate");
            Calendar cl = Calendar.getInstance();
            cl.set(Calendar.DAY_OF_MONTH, 29);

            BasicDBObject query = new BasicDBObject("date", new BasicDBObject("$gt", cl.getTime()));

            System.out.println(dbCollection.find(query).toArray());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void delete() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testDate");
            BasicDBObject match = new BasicDBObject("age", 18);
            dbCollection.remove(match);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void find(){
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testCollection");
            BasicDBObject query = new BasicDBObject("address.country", "china");
            System.out.println(dbCollection.find(query).sort(new BasicDBObject("age", -1)).toArray());
            // 查询部分字段.
            //DBObject keys = new BasicDBObject("age",1).append("name",1).append("_id",0);
            //System.out.println(dbCollection.find(query, keys).toArray());

            DBObject keys = new BasicDBObject().append("_id", 0);

            BasicDBObject query2 = new BasicDBObject("age", 18);

            DBCursor cursor = dbCollection.find(query2, keys);

            System.out.println(cursor.toArray());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void findAnd() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testCollection");
            BasicDBObject query1 = new BasicDBObject("address.country", "china");
            BasicDBObject query2 = new BasicDBObject("age", 18);

            BasicDBObject query = new BasicDBObject("$and", Arrays.asList(query2));

            System.out.println(dbCollection.find(query).toArray());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void findByComparator() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testCollection");
            BasicDBObject query = new BasicDBObject("age", new BasicDBObject("$gte", 20));
            System.out.println(dbCollection.find(query).sort(new BasicDBObject("age", -1)).toArray());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void findByConnOperator() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testCollection");

            // 两次查询结果不一致.
            List<BasicDBObject> list = new ArrayList<>();
            list.add(new BasicDBObject("age", new BasicDBObject("$gte", 20)));
            list.add(new BasicDBObject("age", new BasicDBObject("$lte", 25)));
            BasicDBObject query = new BasicDBObject("$and", list);

            System.out.println(dbCollection.find(query).toArray());

            // 两次查询结果不一致.
            BasicDBObject query1 = new BasicDBObject()
                    .append("age", new BasicDBObject("$gte", 20))
                    .append("age", new BasicDBObject("$lte", 25));


            BasicDBList dbList = new BasicDBList();
            dbList.add(new BasicDBObject("age", new BasicDBObject("$gte", 20)));
            dbList.add(new BasicDBObject("age", new BasicDBObject("$lte", 25)));
            BasicDBObject query2 = new BasicDBObject("$and", dbList);
            System.out.println(dbCollection.find(query2).toArray());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void update() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testCollection");
            BasicDBObject query = new BasicDBObject("name", "himongo");
            BasicDBObject updatedValue = new BasicDBObject();
            updatedValue.put("name", "dd");

            DBObject updateSetValue = new BasicDBObject("$set", updatedValue);
            //DBObject q, DBObject o, boolean upsert(若为true,没有找到匹配的记录时会执行新增一条记录), boolean multi(false,表示只更新查询到的一条记录,)
            dbCollection.update(query, updateSetValue, false, true);
            // 如果不加"$set"做包装,整个对象将会被updatedValue替换掉,而不仅仅只是名字.
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void complexQuery() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testCollection");
            // create our pipeline operations, first with the $match
            DBObject match = new BasicDBObject("$match", new BasicDBObject("gender", 1));

            // build the $projection operation
            DBObject fields = new BasicDBObject("name", 1);
            fields.put("age", 1);
            fields.put("address.country", 1);
            fields.put("_id", 1);
            DBObject project = new BasicDBObject("$project", fields);

            // Now the $group operation
            //DBObject groupFields = new BasicDBObject( "_id", "$name");
            //append("_id", "$_id").
            DBObject groupFields = new BasicDBObject("_id", new BasicDBObject("name", "$name").append("addr", "$address.country"));
            // avg
            //groupFields.put("age", new BasicDBObject( "$avg", "$age"));
            //max
            //groupFields.put("age", new BasicDBObject( "$max", "$age"));
            //min
            //groupFields.put("age", new BasicDBObject( "$min", "$age"));
            //sum
            //groupFields.put("age", new BasicDBObject( "$sum", "$age"));
            //last,first
            groupFields.put("age", new BasicDBObject("$first", "$age"));
            DBObject group = new BasicDBObject("$group", groupFields);

            // Finally the $sort operation
            DBObject sort = new BasicDBObject("$sort", new BasicDBObject("age", 1));

            //DBObject skip = new BasicDBObject("$skip", 1);

            DBObject limit = new BasicDBObject("$limit", 2);

            // run aggregation
            //List<DBObject> pipeline = Arrays.asList(match, project, group, sort, limit);
            //List<DBObject> pipeline = Arrays.asList(match,  group, sort, limit);

            List<DBObject> pipeline = Arrays.asList( sort, limit, group);
            AggregationOutput output = dbCollection.aggregate(pipeline);

            AggregationOptions aggregationOptions = AggregationOptions.builder()
                    .batchSize(100)
                    .outputMode(AggregationOptions.OutputMode.CURSOR)
                    .allowDiskUse(true)
                    .build();

            for (DBObject result : output.results()) {
                System.out.println(result);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }


    @Test
    public void aggregationVivi() throws Exception {
        MongoClient client = new MongoClient("192.168.25.83", 27017);

        DB db = client.getDB("charts1");
        DBCollection dbCollection = db.getCollection("chart");
        DBObject groupFields = new BasicDBObject("_id", new BasicDBObject("settledtime", "$settledtime").append("adtype", "$adtype"));
        DBObject group = new BasicDBObject("$group", groupFields);

        // Finally the $sort operation
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("_id.settledtime", 1).append("_id.adtype",1));

        List<DBObject> pipeline = Arrays.asList(group, sort);

        AggregationOutput output = dbCollection.aggregate(pipeline);

        for (DBObject result : output.results()) {
            System.out.println(result);
        }

        client.close();
    }


    @Test
    public void orderByVivi() throws Exception {
        MongoClient client = new MongoClient("192.168.25.83", 27017);

        DB db = client.getDB("charts1");
        DBCollection dbCollection = db.getCollection("chart");

        DBCursor cursor = dbCollection.find().sort(new BasicDBObject("settledtime", 1).append("province",1));
        for(Object obj : cursor.toArray()){
            System.out.println(obj);
        }
        client.close();
    }

    @Test
    public void count(){
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testCollection");

            DBObject groupFields = new BasicDBObject("_id", new BasicDBObject("name", "$name"));
            groupFields.put("count", new BasicDBObject("$sum", 1));

            DBObject group = new BasicDBObject("$group", groupFields);

            List<DBObject> pipeline = Arrays.asList(group);

            AggregationOutput output = dbCollection.aggregate(pipeline);
            for (DBObject result : output.results()) {
                System.out.println(result);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void count1(){
        MongoClient client = null;
        try {
            client = new MongoClient("192.168.24.29", 27017);
            DB db = client.getDB("myTest");
            DBCollection dbCollection = db.getCollection("testCollection");

            DBObject groupFields = new BasicDBObject("_id", new BasicDBObject("sdomain", "$sdomain")
                    .append("sparameter", "$sparameter"));
            groupFields.put("count", new BasicDBObject("$sum", 1));

            DBObject group = new BasicDBObject("$group", groupFields);

            List<DBObject> pipeline = Arrays.asList(group);

            AggregationOutput output = dbCollection.aggregate(pipeline);
            for (DBObject result : output.results()) {
                System.out.println(result);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    @Test
    public void mapReduce() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testCollection");

            String map = "function() { " +
                    "emit({name:this.name,addr:this.address.country}, this.age);}";

            String reduce = "function(key, values) { return Array.sum(values);} ";

            MapReduceCommand cmd = new MapReduceCommand(dbCollection, map, reduce,
                    null, MapReduceCommand.OutputType.INLINE, null);

            MapReduceOutput out = dbCollection.mapReduce(cmd);

            for (DBObject o : out.results()) {
                System.out.println(o.toString());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

}
