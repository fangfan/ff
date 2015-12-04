package jdk;

import org.junit.Test;
import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by F.Fang on 2015/12/2.
 */
public class JdkProxyTest {

    @Test
    public void demo(){

        IHelloService target = new JdkHelloService();
        HelloInvocationHandler handler = new HelloInvocationHandler(target);

        IHelloService proxy = (IHelloService) handler.createProxy();
        proxy.hi("real");

        System.out.println("proxy is:" + proxy.getClass());

        byte[] classBytes = ProxyGenerator.generateProxyClass(proxy.getClass().getName(),JdkHelloService.class.getInterfaces());
        String path = System.getProperty("user.dir")+File.separator+"ProxyHi.txt";
        File classFile = new File(path);
        FileOutputStream out = null;
        try {
            classFile.createNewFile();
            out = new FileOutputStream(classFile);
            out.write(classBytes);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
