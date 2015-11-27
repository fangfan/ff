package mockito;

import static org.mockito.Matchers.any;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.wit.service.StaticUserService;
import org.wit.service.UserAction;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StaticUserService.class})
public class MockForPrivateStaticDemo {
    
    @Test
    public void demo() throws Exception {
        PowerMockito.spy(StaticUserService.class);
        //模拟返回值私有方法.
        //PowerMockito.when(userService, "secreteSayHello", any(String.class)).thenReturn("mock");
        PowerMockito.doReturn("mock").when(StaticUserService.class, "secreteSayHello", any(String.class));
        //模拟私有空方法.
        PowerMockito.doNothing().when(StaticUserService.class, "secreteSayHi", any(String.class));
        
        // 执行业务方法.
        UserAction userAction = new UserAction();
        userAction.executeForPrivateStatic("real");
        userAction.executeForPrivateStatic("real");
        
        // 验证私有方法.
        PowerMockito.verifyPrivate(StaticUserService.class,Mockito.times(2)).invoke("secreteSayHello", any(String.class));
        PowerMockito.verifyPrivate(StaticUserService.class,Mockito.times(2)).invoke("secreteSayHi", any(String.class));
    }

}
