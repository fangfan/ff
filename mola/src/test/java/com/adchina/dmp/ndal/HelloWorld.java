package com.adchina.dmp.ndal;

import com.mongodb.*;
import org.junit.Test;

/**
 * Created by F.Fang on 2015/3/12.
 * Version :2015/3/12
 */
public class HelloWorld {

    @Test
    public void query() throws Exception {
        // 新建客户端.
        MongoClient client = new MongoClient("127.0.0.1", 27017);
        // 获取DB.
        DB db = client.getDB("testDB");
        // 获取文档集.
        DBCollection collection = db.getCollection("demo");
        // 条件: { "name" : "someone"}
        BasicDBObject match = new BasicDBObject("name", "someone");
        // 执行查询并打印结果集列表.
        System.out.println(collection.find(match).toArray());
        // 关闭客户端
        client.close();
    }

    @Test
    public void insert() throws Exception {
        // 新建客户端.
        MongoClient client = new MongoClient("127.0.0.1", 27017);
        // 获取DB.
        DB db = client.getDB("testDB");
        // 获取文档集.
        DBCollection collection = db.getCollection("demo");
        // 文档.
        BasicDBObject user = new BasicDBObject("name", "someone")
                .append("age", 18)
                .append("gender", 1)
                .append("address", new BasicDBObject("country", "china").append("city", "shanghai"));
        // 打印uid.
        System.out.println(collection.insert(user));
        // 关闭客户端
        client.close();
    }

    @Test
    public void update() throws Exception {
        // 新建客户端.
        MongoClient client = new MongoClient("127.0.0.1", 27017);
        // 获取DB.
        DB db = client.getDB("testDB");
        // 获取文档集.
        DBCollection collection = db.getCollection("demo");
        // 条件.
        // json: {"name" : "someone"}
        BasicDBObject match = new BasicDBObject("name", "someone");
        BasicDBObject updatedValue = new BasicDBObject();
        updatedValue.put("age", 28);
        DBObject updateSetValue = new BasicDBObject("$set", updatedValue);
        //update api: DBObject q, DBObject o, boolean upsert, boolean multi.
        //upsert:若为true,没有找到匹配的记录时会执行新增一条记录,false则不做任何处理.
        //multi:若为false,表示只更新查询到的一条记录,true则表示更新所有匹配记录.
        System.out.println(collection.update(match, updateSetValue, false, true));
        // 关闭客户端
        client.close();
    }

    @Test
    public void delete() throws Exception {
        // 新建客户端.
        MongoClient client = new MongoClient("127.0.0.1", 27017);
        // 获取DB.
        DB db = client.getDB("testDB");
        // 获取文档集.
        DBCollection collection = db.getCollection("demo");
        // 条件.
        // json: {"name" : "someone"}
        BasicDBObject match = new BasicDBObject("name", "someone");
        // 删除匹配记录.
        System.out.println(collection.remove(match));
        // 关闭客户端
        client.close();
    }
}
