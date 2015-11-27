package mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wit.service.StaticUserService;
import org.wit.service.UserAction;

@RunWith(SpringJUnit4ClassRunner.class)
@PrepareForTest({UserAction.class,StaticUserService.class})
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
"javax.management.*" })
public class MockForSpringJunitDemo2 {

    @Autowired
    private UserAction userAction;
    
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void demo() throws Exception {
        
        PowerMockito.spy(StaticUserService.class);
        //模拟返回值的public T static 方法.
        when(StaticUserService.sayHello(any(String.class))).thenReturn("mock");
        //模拟无返回值的public void static 方法
        PowerMockito.doNothing().when(StaticUserService.class, "sayHi", anyString());
        
        // 业务方法调用.
        userAction.executeForPublicStatic("public static");
        
        // 验证 sayHello.
        PowerMockito.verifyStatic(Mockito.times(1));
        StaticUserService.sayHello(anyString());
        
        // 验证 sayHi.
        PowerMockito.verifyStatic(Mockito.times(1));
        StaticUserService.sayHi(anyString());
    }

}
