package org.wit.ff.cache;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

/**
 * Created by F.Fang on 2015/9/24.
 * Version :2015/9/24
 */
public class XMemcachedTest {

    @Test
    public void demo(){
        XMemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("30.85.178.212:21212"));

        MemcachedClient client = null;
        try {
            client = builder.build();
            client.add("hi",0,"hello world");
            TimeUnit.SECONDS.sleep(2);
            assertEquals("hello world", client.get("hi"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        } finally {
            if(client!=null){
                try {
                    client.shutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
