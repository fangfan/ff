package jmockit;

import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wit.service.StaticUserService;
import org.wit.service.UserAction;
import org.wit.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MockForSpringJunitDemo {

    @Autowired
    private UserAction userAction;
    
    @Autowired
    private UserService userService;

    @Test
    public void demo() throws Exception {
        new NonStrictExpectations(StaticUserService.class) {
            {
                StaticUserService.sayHello(anyString);
                result = "mock";
            }
        };

        userAction.executeForPublicStatic1("real");

        new Verifications() {
            {
                StaticUserService.sayHello(anyString);
                times = 1;
            }
        };
        
    }
    
    /**
     * 
     * <pre>
     * 模拟bean的方法.
     * UserService sayHello模拟调用, sayHi为真实调用.
     * </pre>
     *
     * @throws Exception
     */
    @Test
    public void beanDemo() throws Exception{
        new NonStrictExpectations(userService) {
            {
                userService.sayHello(anyString);
                result = "mock";
            }
        };

        userAction.executeForPublic("hi");

        new Verifications() {
            {
                userService.sayHello(anyString);
                times = 1;
            }
        };
        
    }
    
}
