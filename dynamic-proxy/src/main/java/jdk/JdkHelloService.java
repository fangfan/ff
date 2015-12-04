package jdk;

/**
 * Created by F.Fang on 2015/12/2.
 */
public class JdkHelloService implements IHelloService {
    public String hi(String msg) {
        return "hello world, msg="+msg;
    }
}
