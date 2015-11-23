package org.wit.ff.jdbc;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.wit.ff.jdbc.access.IDataAccessor;
import org.wit.ff.jdbc.converter.ParamsConverter;
import org.wit.ff.jdbc.sql.db.MySQLBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by F.Fang on 2015/3/31.
 * Version :2015/3/31
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CRUDTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    private IDataAccessor dataAccessor;

    @Test
    public void query(){
        MySQLBuilder builder = new MySQLBuilder();
        // sql: select * from Audience limit 2
        builder.SELECT("*").FROM("Audience").PAGE(0, 2);
        String sql = builder.toString();
        final Audience audience = new Audience();
        audience.setName("ff");

        // 也可以直接填写SQL.
        List<Audience> list = dataAccessor.query(sql, null, Audience.class);
        System.out.println(list);
    }

    @Test
    public void insert(){
        // 批量新增.
        String sql = "insert into audience(id,name,pay) values(?,?,?)";
        Object[][] params = new Object[1][];
        params[0] = new Object[]{100,"ff",100.1};
        dataAccessor.insert(sql, params);
    }

    @Test
    public void insertDate(){
        // 试试日期.
        String sql = "insert into test_date(id,current) values(?,?)";
        Object[][] params = new Object[1][];
        params[0] = new Object[]{100, new Date()};
        dataAccessor.insert(sql, params);
    }

    @Test
    public void insertWithParamsConverter(){
        // 试试PramsConverter好用不.
        ParamsConverter<Audience> converter = new ParamsConverter<Audience>() {
            @Override
            public Object[] convert(Audience audience) {
                return new Object[]{audience.getId(), audience.getName(), audience.getPay()};
            }
        };

        String sql = "insert into audience(id,name,pay) values(?,?,?)";
        // 构造数据.
        List<Audience> list = new ArrayList<>();
        Audience audience1 = new Audience();
        audience1.setId(250);
        audience1.setName("ff");
        audience1.setPay(1000.00);

        Audience audience2 = new Audience();
        audience2.setId(251);
        audience2.setName("ff1");
        audience2.setPay(1000.00);

        list.add(audience1);
        list.add(audience2);

        dataAccessor.insert(sql, list, Audience.class, converter);

    }

}
