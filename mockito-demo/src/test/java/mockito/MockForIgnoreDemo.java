package mockito;

import static org.mockito.Matchers.any;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.wit.service.UserAction;
import org.wit.service.UserService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UserService.class,UserAction.class})
@PowerMockIgnore("java.xml.*")
public class MockForIgnoreDemo {

    //private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MockForIgnoreDemo.class);
    
    private static final Logger LOG = Logger.getLogger(MockForIgnoreDemo.class);

    @Test
    public void demo() throws Exception{
        LOG.info("start!");
        UserService userService = PowerMockito.spy(new UserService());

        // 模拟返回值私有方法.
        PowerMockito.doReturn("mock").when(userService, "secreteSayHello", any(String.class));
        // 模拟私有空方法.
        PowerMockito.doNothing().when(userService, "secreteSayHi", any(String.class));
        
        // 设置业务服务.
        UserAction userAction = new UserAction();
        userAction.setUserService(userService);
        
        // 调用业务方法.
        userAction.executeForPrivate("private");
        
        // 验证.
        PowerMockito.verifyPrivate(userService, Mockito.times(1)).invoke("secreteSayHello", any(String.class));
        PowerMockito.verifyPrivate(userService, Mockito.times(1)).invoke("secreteSayHi", any(String.class));
        LOG.info("end!");
    }

}
