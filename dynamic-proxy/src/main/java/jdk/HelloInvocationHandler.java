package jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by F.Fang on 2015/12/2.
 */
public class HelloInvocationHandler implements InvocationHandler{
    /**
     * 被代理的目标对象.
     */
    private Object target;

    public HelloInvocationHandler(Object target) {
        super();
        this.target = target;
    }

    public Object createProxy(){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target.getClass().getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 如果是proxy(默认执行toString())将抛出java.lang.StackOverflowError.
        // System.out.println("i am your agent, proxy is:"+proxy);
        System.out.println("i am your agent, proxy is:"+proxy.getClass());
        return method.invoke(target, args);
    }

}
