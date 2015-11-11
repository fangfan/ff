package org.wit.ff.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by F.Fang on 2015/11/10.
 * Version :2015/11/10
 */

@ContextConfiguration(locations = "classpath:spring-log-aop.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class BusinessLogTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    private CustomerBusiness customerBusiness;

    @Test
    public void demo() throws Exception {
        customerBusiness.getCustomer(1,1);
        Thread.sleep(10000);
    }

}
