package org.wit.ff.jdbc;


import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.wit.ff.jdbc.core.IDataAccessor;
import org.wit.ff.jdbc.sql.mysql.MySQLBuilder;

import java.util.Date;
import java.util.List;

/**
 * Created by F.Fang on 2015/3/31.
 * Version :2015/3/31
 */
public class CRUDTest {

    @Test
    public void demo(){
        MySQLBuilder builder = new MySQLBuilder();
        builder.SELECT("*").FROM("Audience").PAGE(0,2);
        String sql = builder.toString();
        final Audience audience = new Audience();
        audience.setName("ff");

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        IDataAccessor accessor = (IDataAccessor)context.getBean("dataAccessor");

        List<Audience> list = accessor.query(sql, null, Audience.class);
        System.out.println(list);

//        list = accessor.query("select * from audience where id in(?,?)", new Object[]{1,2}, Audience.class);
//        System.out.println(list);

    }

    @Test
    public void insert(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        IDataAccessor accessor = (IDataAccessor)context.getBean("dataAccessor");
        String sql = "insert into audience(id,name,pay) values(?,?,?)";
        Object[][] params = new Object[1][];
        params[0] = new Object[]{100,"ff",100.1};
        accessor.insert(sql, params);
    }

    @Test
    public void insertDate(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        IDataAccessor accessor = (IDataAccessor)context.getBean("dataAccessor");
        String sql = "insert into test_date(id,current) values(?,?)";
        Object[][] params = new Object[1][];
        params[0] = new Object[]{100, new Date()};
        accessor.insert(sql, params);
    }

}
