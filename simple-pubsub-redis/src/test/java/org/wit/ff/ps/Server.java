package org.wit.ff.ps;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Server {

    @SuppressWarnings({ "unused", "resource" })
    public static void main(String[] args) throws Exception{
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Thread.sleep(20000L);
        //context.close();
    }

}
