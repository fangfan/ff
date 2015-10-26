package org.wit.ff.cache;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.wit.ff.business.UserBusiness;
import org.wit.ff.model.User;

/**
 * Created by F.Fang on 2015/10/26.
 * Version :2015/10/26
 */
@ContextConfiguration("classpath:spring.xml")
public class CacheIntegrationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private UserBusiness userBusiness;

    @Test
    public void demo(){
        User user1 = userBusiness.findUnique(1,1000);
        System.out.println(user1);
        userBusiness.saveUser(1, new User());

        User user2 = userBusiness.findUnique(1,1000);
        System.out.println(user2);
        userBusiness.saveUser(1, new User());
    }

}
