package com.adchina.dmp.ndal;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * Created by F.Fang on 2015/3/12.
 * Version :2015/3/12
 */
public class DateTest {

    @Test
    public void testDate() {
        MongoClient client = null;
        try {
            client = new MongoClient("127.0.0.1", 27017);
            DB db = client.getDB("testDB");
            DBCollection dbCollection = db.getCollection("testDate");
            BasicDBObject obj = new BasicDBObject("name", "dd")
                    .append("age", 18)
                    .append("gender", 1)
                    .append("createdDate", new Date());
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
    public void demoUTC() throws Exception {
        String format = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'";
        String dateStr = "2015-02-27T10:36:46.020Z";

        SimpleDateFormat utcSdf = new SimpleDateFormat(format);
        utcSdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = utcSdf.parse(dateStr);
        System.out.println(date);

        SimpleDateFormat iSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        iSdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        System.out.println(iSdf.format(new Date()));

        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(sdfYMD.format(date));
    }

    @Test
    public void testDate1() throws Exception {
        String dateStr = "10000-01-01 09:00:00.0";
        String format = "yyyy-MM-dd HH:mm:ss.sss";

        SimpleDateFormat utcSdf = new SimpleDateFormat(format);
        utcSdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = utcSdf.parse(dateStr);
        System.out.println(date);
    }
    
    @Test
    public void demo(){
    	Map<Long, String> map1 = new HashMap<Long,String>();
    	Map<Integer, String> map2 = new HashMap<Integer, String>();
    	map1.put(1L, "aa");
    	
    	map2.put(1, "bb");
    	
    	int id = 1;
    	System.out.println(map1.containsKey(id));
    	System.out.println(map2.containsKey(id));
    }

}
