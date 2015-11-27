package mockito;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.wit.service.PrivateConstructorService;
import org.wit.service.UserAction;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PrivateConstructorService.class,UserAction.class})
public class MockForPrivateConstrutorDemo {
    
    @Test
    public void demo() throws Exception {
        
        PowerMockito.spy(PrivateConstructorService.class);
        // 使用PowerMockito创建拥有私有构造函数类的实例
        PrivateConstructorService instance = PowerMockito.constructor(PrivateConstructorService.class).newInstance(new Object[]{});
        // 模拟静态函数.
        when(PrivateConstructorService.createInstance()).thenReturn(instance);
        
        // 业务方法调用.
        UserAction userAction = new UserAction();
        userAction.executeForPrivateConstrutor("real");
        
        // 验证 sayHello.
        PowerMockito.verifyStatic(Mockito.times(1));
        PrivateConstructorService.createInstance();
    }

}
