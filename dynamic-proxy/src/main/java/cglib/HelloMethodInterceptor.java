package cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by F.Fang on 2015/12/2.
 * Spring当中也存在MethodInterceptor.
 * org.aopalliance.intercept.MethodInterceptor;
 */
public class HelloMethodInterceptor implements MethodInterceptor{

    private Object target;

    public HelloMethodInterceptor(Object target){
        this.target = target;
    }

    public Object createProxy(){
        Enhancer enhancer = new Enhancer();
        // 生成子类.
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("i am your agent");
        return methodProxy.invokeSuper(o,objects);
    }
}
