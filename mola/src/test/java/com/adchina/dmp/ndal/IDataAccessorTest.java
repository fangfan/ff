package com.adchina.dmp.ndal;

import com.adchina.dmp.ndal.model.DateUser;
import com.adchina.dmp.ndal.model.MutiAdUser;
import com.adchina.dmp.ndal.model.User;
import com.adchina.dmp.ndal.mongodb.DataAccessorImpl;
import com.adchina.dmp.ndal.mongodb.cri.Criteria;
import com.adchina.dmp.ndal.mongodb.cri.CriteriaChain;
import com.adchina.dmp.ndal.mongodb.cri.CriteriaGroup;
import com.adchina.dmp.ndal.mongodb.query.Aggregation;
import com.adchina.dmp.ndal.mongodb.query.AggregationQuery;
import com.adchina.dmp.ndal.mongodb.query.BasicQuery;
import com.mongodb.MongoClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by F.Fang on 2015/3/2.
 * Version :2015/3/2
 */
public class IDataAccessorTest {

    private MongoClient client;

    private DataAccessorImpl dataAccessor;

    @Before
    public void before(){
        try{
            client= new MongoClient("127.0.0.1", 27017);
            dataAccessor = new DataAccessorImpl();
            dataAccessor.setClient(client);
            dataAccessor.setDbName("testDB");
        }catch(Exception e){

        }
    }

    @Test
    public void demo() throws Exception {
        /**
         * select *(_id) from testCollection where age=18;
         */
        Map<String,Object> params = new HashMap<>();
        params.put("age",18);

        // 查询条件.
        CriteriaChain cc = new CriteriaChain();
        cc.append(new CriteriaGroup().append(new Criteria("age").eq())).PARAM(params);

        // query.
        BasicQuery query = new BasicQuery("testCollection",User.class);
        query.WHERE(cc).build();

        Collection<User> users = dataAccessor.query(query);
        for(User user: users){
            System.out.println(user);
        }
    }

    @Test
    public void demoList() throws Exception{
        // query.
        BasicQuery query = new BasicQuery("testAttrList",MutiAdUser.class);
        // 查询条件.
        CriteriaChain cc = new CriteriaChain();
        cc.append(new CriteriaGroup().append(new Criteria("address.city","上海").eq()));
        query.WHERE(cc).build();

        Collection<MutiAdUser> results = dataAccessor.query(query);
        for(MutiAdUser result: results){
            System.out.println(result);
        }

    }

    @Test
    public void demoDate() throws Exception{
        // query.
        BasicQuery query = new BasicQuery("testDate",DateUser.class);
        query.build();
        Collection<DateUser> results = dataAccessor.query(query);
        for(DateUser user: results){
            System.out.println(user);
        }
    }


    @Test
    public void demoForVivi() throws Exception{

        MongoClient client = new MongoClient("192.168.25.83", 27017);

        dataAccessor.setClient(client);
        dataAccessor.setDbName("charts1");

        AggregationQuery aggregationQuery = new AggregationQuery("chart", LinkedHashMap.class);

        aggregationQuery.GROUP("settledtime,adtype").SORT_ASC("settledtime,adtype").build();

        Collection<LinkedHashMap> users = dataAccessor.query(aggregationQuery);

        System.out.println("-------------------------");
        for(LinkedHashMap user: users){
            System.out.println(user);
        }
    }

    @Test
    public void queryOneKeyWithMoreParams(){

        // 查询条件.
        CriteriaChain cc = new CriteriaChain();

        CriteriaGroup group1 = new CriteriaGroup()
                .append(new Criteria("age",18).gt())
                .append(new Criteria("age",25).lt())
                .AND();

        cc.append(group1);

        // query.
        BasicQuery query = new BasicQuery("testCollection",User.class);
        query.WHERE(cc).build();

        Collection<User> users = dataAccessor.query(query);
        for(User user: users){
            System.out.println(user);
        }
    }

    @Test
    public void queryMap(){
        // 查询条件.
        CriteriaChain cc = new CriteriaChain();

        CriteriaGroup group1 = new CriteriaGroup()
                .append(new Criteria("age",18).gt())
                .append(new Criteria("age",25).lt())
                .AND();

        cc.append(group1);

        // query.
        BasicQuery query = new BasicQuery("testCollection",HashMap.class);
        query.WHERE(cc).build();

        Collection<HashMap> users = dataAccessor.query(query);
        for(HashMap user: users){
            System.out.println(user);
        }
    }

    @Test
    public void queryOrder() throws Exception {
        // 查询条件.
        client = new MongoClient("192.168.25.83", 27017);
        dataAccessor.setDbName("charts");
        dataAccessor.setClient(client);

        // query.
        BasicQuery query = new BasicQuery("chart",HashMap.class);
        query.SORT_DESC("settledtime").PAGE(0,20).build();

        Collection<HashMap> users = dataAccessor.query(query);
        for(HashMap user: users){
            System.out.println(user);
        }
    }

    @Test
    public void aggregationQuery(){
        CriteriaChain cc = new CriteriaChain();

        CriteriaGroup group1 = new CriteriaGroup()
                .append(new Criteria("age",18).gt())
                .append(new Criteria("age",25).lt())
                .AND();

        cc.append(group1);

        AggregationQuery aggregationQuery = new AggregationQuery("testCollection", User.class);

        aggregationQuery.WHERE(cc).PAGE(0,10);

        aggregationQuery.GROUP("name").AG(new Aggregation("age").sum()).build();

        Collection<User> users = dataAccessor.query(aggregationQuery);
        for(User user: users){
            System.out.println(user);
        }

    }

    @Test
    public void testMap(){
        CriteriaChain cc = new CriteriaChain();

        CriteriaGroup group1 = new CriteriaGroup()
                .append(new Criteria("age",18).gt())
                .append(new Criteria("age",25).lt())
                .AND();

        cc.append(group1);

        AggregationQuery aggregationQuery = new AggregationQuery("testCollection", HashMap.class);

        aggregationQuery.WHERE(cc).PAGE(0,10);

        aggregationQuery.GROUP("name").AG(new Aggregation("age").sum()).build();

        Collection<HashMap> users = dataAccessor.query(aggregationQuery);
        for(HashMap user: users){
            System.out.println(user);
        }
    }

    @Test
    public void testCount() throws Exception{
        client = new MongoClient("192.168.24.29", 27017);
        dataAccessor.setDbName("myTest");
        dataAccessor.setClient(client);

        AggregationQuery aggregationQuery = new AggregationQuery("testCollection", HashMap.class);

        aggregationQuery.GROUP("sdomain,sparameter").AG(new Aggregation("count").count()).build();
        Collection<HashMap> result = dataAccessor.query(aggregationQuery);

        if(result!=null) {
            for (HashMap map : result) {
                System.out.println(map);
            }
        }

    }

    @After
    public void after(){
        if(client!=null){
            client.close();
        }
    }

}
