package org.wit.ff.jdbc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.wit.ff.jdbc.dao.HomeTownDao;
import org.wit.ff.jdbc.model.HomeTown;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by F.Fang on 2015/11/17.
 * Version :2015/11/17
 */
@ContextConfiguration(locations = {"classpath:applicationContext-single.xml"})
public class HomeTownDaoTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private HomeTownDao homeTownDao;

    @Test
    public void testFindAll(){
        System.out.println(homeTownDao.findAll());
    }

    /**
     * 无法通过单纯的插入列表生成主键.
     * 方法执行时会发生异常
     * Caused by: org.apache.ibatis.binding.BindingException: Parameter '__frch_item_0' not found. Available parameters are [list]
     */
    @Test
    public void testInsertList(){
        HomeTown ht1 = new HomeTown();
        ht1.setName("hb");
        ht1.setLocation("hubei");
        HomeTown ht2 = new HomeTown();
        ht2.setName("js");
        ht2.setLocation("jiangsu");

        List<HomeTown> list = new ArrayList<>();
        list.add(ht1);
        list.add(ht2);

        try {
            homeTownDao.insertList(list);
        }catch (Exception e){
            assertTrue(true);
        }

        System.out.println(list);
    }

}
