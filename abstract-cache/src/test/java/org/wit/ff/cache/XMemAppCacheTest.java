package org.wit.ff.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tmodel.User;

import java.util.concurrent.TimeUnit;

/**
 * Created by F.Fang on 2015/9/24.
 * Version :2015/9/24
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-memcached.xml"})
public class XMemAppCacheTest {

    @Autowired
    private IAppCache appCache;

    @Test
    public void demo() throws Exception{
        appCache.put("ff", new User(1, "ff", "ff@adchina.com"));
        TimeUnit.SECONDS.sleep(3);
        User user = appCache.get("ff",User.class);
        System.out.println(user);
    }

}
