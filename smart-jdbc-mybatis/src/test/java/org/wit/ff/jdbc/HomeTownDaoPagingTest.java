package org.wit.ff.jdbc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.wit.ff.jdbc.dao.HomeTownDao;
import org.wit.ff.jdbc.query.Criteria;
import org.wit.ff.jdbc.result.CriteriaResultHolder;

/**
 * Created by F.Fang on 2015/11/19.
 */
@ContextConfiguration(locations = {"classpath:applicationContext-paging.xml"})
public class HomeTownDaoPagingTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private HomeTownDao homeTownDao;

    @Test
    public void testFindAll() {
        System.out.println(homeTownDao.findAll());
    }

    @Test
    public void testFind() {
        //System.out.println(homeTownDao.find(1, new Criteria().page(1, 10)));
        System.out.println(homeTownDao.find(1, null));
        System.out.println(CriteriaResultHolder.get());
    }

}
