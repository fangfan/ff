package com.adchina.dmp.ndal.mongodb;

import com.adchina.dmp.ndal.model.MutiAdUser;
import com.adchina.dmp.ndal.mongodb.MongoUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.Map;

/**
 * Created by F.Fang on 2015/3/3.
 * Version :2015/3/3
 */
public class MongoUtilsTest {

    @Test
    public void fillMap(){
        BasicDBList dbList = new BasicDBList();
        dbList.add(new BasicDBObject("country", "china").append("city", "shanghai"));
        dbList.add(new BasicDBObject("country", "china").append("city", "beijing"));

        BasicDBObject obj = new BasicDBObject("name", "himongo")
                .append("age", 18)
                .append("gender", 1)
                .append("address", dbList);

        System.out.println(MongoUtils.translate(obj));

        MutiAdUser result = MongoUtils.build(obj, MutiAdUser.class);

        System.out.println(result);
    }

    @Test
    public void testMap(){
        BasicDBObject obj = new BasicDBObject("name", "himongo")
                .append("age", 18)
                .append("gender", 1)
                .append("address", new BasicDBObject("country", "china").append("city", "shanghai"));
        Map map = obj.toMap();
        System.out.println(map);
    }



}
