package mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.lang.reflect.Constructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.wit.service.BeanUtilsInvoker;
import org.wit.service.UserAction;
import org.wit.service.UserService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BeanUtils.class, BeanUtilsInvoker.class})
//@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
//                "javax.management.*" })
public class MockForSpringBeanDemo1 {
    @Test
    public void demo() throws Exception {
        PowerMockito.spy(BeanUtils.class);
        UserService service = PowerMockito.spy(new UserService());
        service.setTag("mock");
        //有返回值的方法
        when(service.sayHello(any(String.class))).thenReturn("mock sayHello!");
        
        Constructor<UserService> cst = PowerMockito.constructor(UserService.class);
        PowerMockito.when(BeanUtils.instantiateClass(cst,  new Object[]{})).thenReturn(service);
        
        // 参数必须全部是matcher或者全部是具体值.
        //PowerMockito.when(BeanUtils.instantiateClass(cst,  Mockito.eq(new Object[]{}))).thenReturn(service);
        
        // 核心在于在其创建对象时.
//        UserService result =(UserService) new BeanUtilsInvoker().invoke(cst, new Object[]{});
//        System.out.println(result.getTag());
        
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                        "classpath:applicationContext.xml");
        UserAction action = applicationContext.getBean(UserAction.class);
        System.out.println(action.getClass().getClassLoader());
        action.execute("real");
        
        PowerMockito.verifyStatic(times(1));
        BeanUtils.instantiateClass(cst,  new Object[]{});
        
        //BeanUtils.instantiateClass(cst,  Mockito.eq(new Object[]{}));
    }

}
