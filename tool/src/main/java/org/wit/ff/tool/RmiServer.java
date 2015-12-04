package org.wit.ff.tool;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * Created by F.Fang on 2015/11/30.
 */
public class RmiServer {

    public static void main(String[] args) throws Exception{

        LocateRegistry.createRegistry(1099);

        //创建远程对象的一个或多个实例，下面是hello对象
        //可以用不同名字注册不同的实例
        IHelloService service = new HelloService();

        //把hello注册到RMI注册服务器上，命名为Hello
        Naming.rebind("Hello", service);

        System.out.println("server is running!");

        //Thread.sleep(100000);

    }
}
