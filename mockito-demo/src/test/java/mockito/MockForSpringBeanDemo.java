package mockito;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.wit.service.UserAction;
import org.wit.service.UserService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ UserAction.class,  MockForSpringBeanDemo.class , BeanUtils.class})
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
                "javax.management.*" })
public class MockForSpringBeanDemo {

    @Test
    public void demo() {
        UserService userService = PowerMockito.spy(new UserService());
        userService.setTag("mock");
        when(userService.sayHello(anyString())).thenReturn("mock sayHello!");

        // PowerMockito.constructor(UserService.class).

        PowerMockito.spy(UserService.class);
        // 模拟构造函数.
        try {
            PowerMockito.whenNew(UserService.class).withAnyArguments().thenReturn(userService);
        } catch (Exception e) {
            //
        }
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                        "classpath:applicationContext.xml");
        UserAction action = applicationContext.getBean(UserAction.class);
        System.out.println(action.getClass().getClassLoader());
        action.execute("real");
        // 验证构造方法.
        PowerMockito.verifyNew(UserService.class, times(1));
    }
    

    @Test
    public void demo1() {
        UserService userService = PowerMockito.spy(new UserService());
        userService.setTag("mock");
        when(userService.sayHello(anyString())).thenReturn("mock sayHello!");

        PowerMockito.constructor(UserService.class);

        PowerMockito.spy(UserService.class);
        // 模拟构造函数.
        try {
            PowerMockito.whenNew(UserService.class).withAnyArguments().thenReturn(userService);
        } catch (Exception e) {
            //
        }
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                        "classpath:applicationContext.xml");
        UserAction action = applicationContext.getBean(UserAction.class);
        System.out.println(action.getClass().getClassLoader());
        action.execute("real");
        // 验证构造方法.
        PowerMockito.verifyNew(UserService.class, times(1));
    }

    @Test
    public void demo2() throws Exception {
        UserService service = new UserService();
        service.setTag("mock");
        System.out.println(service);
        PowerMockito.mockStatic(UserService.class.getClass());
        //Constructor<UserService> cst = UserService.class.getConstructor(null);
        Constructor<UserService> mst = PowerMockito.mock(Constructor.class);
        PowerMockito.doReturn(service).when(mst, "newInstance", new Object[]{});
        System.out.println(mst);
        //PowerMockito.when(UserService.class.getClass(), "getDeclaredConstructor", new Class<?>[]{}).thenReturn(cst);
        PowerMockito.doReturn(mst).when(UserService.class.getClass(), "getDeclaredConstructor", new Class<?>[]{});
        
        Constructor dd = UserService.class.getDeclaredConstructor(null);
        
        System.out.println(dd.newInstance(null));

    }
    
    @Test
    public void demo3() throws Exception {
        UserService service = new UserService();
        service.setTag("mock");
        PowerMockito.spy(BeanUtils.class);
        Constructor<UserService> cst = PowerMockito.constructor(UserService.class);
        PowerMockito.doReturn(service).when(BeanUtils.instantiateClass(cst, new Object[]{}));
        
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                        "classpath:applicationContext.xml");
        UserAction action = applicationContext.getBean(UserAction.class);
        System.out.println(action.getClass().getClassLoader());
        action.execute("real");
        // 验证构造方法.
     // 验证 sayHi.
        PowerMockito.verifyStatic(Mockito.times(1));
        BeanUtils.instantiateClass(cst, new Object[]{});
    }

}
