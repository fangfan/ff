package cglib;

import org.junit.Test;
import sun.misc.ProxyGenerator;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by F.Fang on 2015/12/2.
 */
public class CglibProxyTest {

    @Test
    public void demo() throws Exception{
        CglibHelloService target = new CglibHelloService();
        HelloMethodInterceptor interceptor = new HelloMethodInterceptor(target);

        CglibHelloService proxy = (CglibHelloService) interceptor.createProxy();

        System.out.println(proxy.hi("real"));

        Class<?> targetClass = proxy.getClass();

        System.out.println(targetClass.getName());

        byte[] classBytes =  ProxyGenerator.generateProxyClass(proxy.getClass().getName(), CglibHelloService.class.getInterfaces());
        String path = System.getProperty("user.dir")+ File.separator+"CglibProxyHi.txt";
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
