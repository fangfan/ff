package mockito;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.wit.service.ConstructorService;
import org.wit.service.UserAction;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConstructorService.class,UserAction.class})
public class MockForConstructorDemo {

    @Test
    public void testMockForConstructor() throws Exception {
        UserAction userAction = new UserAction();
        //模拟构造函数.
        //value必须提前构造.
        //ConstructorService value = new ConstructorService("mock");
        ConstructorService value = PowerMockito.spy(new ConstructorService("mock"));
        // 若定义doNoting方法的规则,则会打印mock的内容:mock doSomething!.
        //when(value.doNoting()).thenReturn("mock doSomething!");
        PowerMockito.spy(ConstructorService.class);
        //模拟构造函数.
        PowerMockito.whenNew(ConstructorService.class).withArguments(anyString()).thenReturn(value);
        //执行业务逻辑.
        userAction.executeForConstructor("real");
        //验证构造方法.
        PowerMockito.verifyNew(ConstructorService.class, times(1)).withArguments(anyString());
    }

}
