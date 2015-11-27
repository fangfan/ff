package mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;

import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.springframework.beans.BeanUtils;
import org.wit.service.BeanUtilsInvoker;
import org.wit.service.UserService;

@PrepareForTest({ BeanUtils.class, BeanUtilsInvoker.class })
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
                "javax.management.*, java.lang.*" })
public class PowerMockRuleDemo {
    
    @Rule
    public PowerMockRule rule = new PowerMockRule();
    
    @Test
    public void demo(){
        PowerMockito.spy(BeanUtils.class);
        UserService service = PowerMockito.spy(new UserService());
        service.setTag("mock");
        //有返回值的方法
        when(service.sayHello(any(String.class))).thenReturn("mock sayHello!");
        
        Constructor<UserService> cst = PowerMockito.constructor(UserService.class);
        // 参数必须全部是matcher或者全部是具体值.
        PowerMockito.when(BeanUtils.instantiateClass(cst,  new Object[]{})).thenReturn(service);
                
        // 核心在于在其创建对象时.
        UserService result =(UserService) new BeanUtilsInvoker().invoke(cst, new Object[]{});
        System.out.println(result.getTag());
        
        PowerMockito.verifyStatic(times(1));
        BeanUtils.instantiateClass(cst,  new Object[]{});
    }

}
