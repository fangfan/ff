package mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

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
public class MockForPublicStaticDemo {

    @Test
    public void demo() throws Exception {
        //mock会模拟所有的方法.
        //PowerMockito.mock(StaticUserService.class);
        //spy只会模拟指定模拟行为的方法.
        PowerMockito.spy(StaticUserService.class);
        //模拟返回值的public T static 方法.
        when(StaticUserService.sayHello(any(String.class))).thenReturn("mock");
        //模拟无返回值的public void static 方法
        PowerMockito.doNothing().when(StaticUserService.class, "sayHi", anyString());
        
        // 业务方法调用.
        UserAction userAction = new UserAction();
        userAction.executeForPublicStatic("public static");
        
        // 验证 sayHello.
        PowerMockito.verifyStatic(Mockito.times(1));
        StaticUserService.sayHello(anyString());
        
        // 验证 sayHi.
        PowerMockito.verifyStatic(Mockito.times(1));
        StaticUserService.sayHi(anyString());
    }

}
