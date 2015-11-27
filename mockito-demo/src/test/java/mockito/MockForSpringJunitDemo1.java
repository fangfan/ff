package mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.wit.service.UserAction;
import org.wit.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@PrepareForTest({ BeanUtils.class})
@ContextConfiguration(locations="classpath:applicationContext.xml")
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*", "javax.xml.parser", "org.xml.*", "org.w3c.*",
                "javax.management.*" })
@TestExecutionListeners({ 
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class })
public class MockForSpringJunitDemo1 {
    

    @Rule
    public PowerMockRule rule = new PowerMockRule();
    
    @Autowired
    private UserAction userAction;

    @Test
    public void demo() throws Exception {
        PowerMockito.mockStatic(BeanUtils.class);
       // PowerMockito.spy(BeanUtils.class);
        UserService service = PowerMockito.spy(new UserService());
        service.setTag("mock");
        // 有返回值的方法
        when(service.sayHello(any(String.class))).thenReturn("mock sayHello!");

        Constructor<UserService> cst = PowerMockito.constructor(UserService.class);
        PowerMockito.when(BeanUtils.instantiateClass(cst, new Object[] {})).thenReturn(service);

        // 参数必须全部是matcher或者全部是具体值.
        // PowerMockito.when(BeanUtils.instantiateClass(cst, Mockito.eq(new
        // Object[]{}))).thenReturn(service);

        // 核心在于在其创建对象时.
        // UserService result =(UserService) new BeanUtilsInvoker().invoke(cst,
        // new Object[]{});
        // System.out.println(result.getTag());

        // ClassPathXmlApplicationContext applicationContext = new
        // ClassPathXmlApplicationContext(
        // "classpath:applicationContext.xml");
        // UserAction action = applicationContext.getBean(UserAction.class);
        System.out.println(userAction.getClass().getClassLoader());
        userAction.execute("real");
        
        PowerMockito.verifyStatic(times(1));
        BeanUtils.instantiateClass(cst, new Object[] {});

    }

}
