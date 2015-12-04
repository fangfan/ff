package org.wit.ff.ps.publisher;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.wit.ff.ps.IMessage;
import org.wit.ff.ps.User;
import org.wit.ff.ps.publisher.redis.PublisherFactory;

public class PublisherTest {
    
    @SuppressWarnings("resource")
    @Test
    public void demo() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-pub.xml");
        IPublisher publisher = (IPublisher) context.getBean("messagePublisher");
        publisher.send("hello.world",new IMessage<String>(){

            @Override
            public String getBody() {
                return "gogogo";
            }

            });
        
    }
    
    @Test
    public void test1() throws Exception {
        IPublisher publisher = new PublisherFactory("192.168.21.125", 6379, "enterprise").getPublisherIntance();
        publisher.send("hello.user", new User("ff",27));
        //Thread.sleep(2000);
        publisher.close();
    }

}
