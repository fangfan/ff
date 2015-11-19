package org.wit.ff.jdbc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.wit.ff.jdbc.dao.HomeTownDao;
import org.wit.ff.jdbc.model.HomeTown;

import java.util.ArrayList;
import java.util.List;

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
     * 无法通过单纯的列表生成主键.
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

        homeTownDao.insertList(list);

        System.out.println(list);
    }

}
