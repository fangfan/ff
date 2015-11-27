package mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wit.service.UserAction;
import org.wit.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MockForSpringJunitDemo {

    @Autowired
    private UserAction userAction;
    
    private UserService mockUserService;

    @Before
    public void init() {
        mockUserService = PowerMockito.spy(new UserService());
        mockUserService.setTag("mock");
        // 有返回值的方法
        when(mockUserService.sayHello(any(String.class))).thenReturn("mock sayHello!");
        userAction.setUserService(mockUserService);
    }

    @Test
    public void demo() throws Exception {
        System.out.println(userAction.getClass().getClassLoader());
        userAction.execute("real");
    }
    
    @After
    public void destroy(){
        verify(mockUserService, times(1)).sayHello(any(String.class));
    }

}
