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
@PrepareForTest({ StaticUserService.class })
public class MockForStaticFinalDemo {

    @Test
    public void demo() throws Exception {
        PowerMockito.spy(StaticUserService.class);
        // 定义模拟规则.
        PowerMockito.doReturn("mock").when(StaticUserService.class, "sayFinal", any(String.class));
        
        // 执行业务方法.
        UserAction userAction = new UserAction();
        userAction.executeForStaticFinal("real");

        // 验证私有方法.
        PowerMockito.verifyPrivate(StaticUserService.class, Mockito.times(1)).invoke("sayFinal",
                        any(String.class));
    }

}
