package org.wit.ff.cache;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import tmodel.User;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by F.Fang on 2015/10/19.
 * Version :2015/10/19
 */
@ContextConfiguration("classpath:spring-redis.xml")
public class AppCacheTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private IAppCache appCache;

    @Test
    public void demo() throws Exception{
        User user = new User(1, "ff", "ff@adchina.com");
        appCache.put("ff", user);
        TimeUnit.SECONDS.sleep(3);
        User result = appCache.get("ff",User.class);
        assertEquals(user, result);
    }

}
