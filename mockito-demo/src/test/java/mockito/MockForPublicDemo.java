package mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import org.wit.service.UserAction;
import org.wit.service.UserService;


public class MockForPublicDemo {

    @Test
    public void demo() {
        UserService userService = mock(UserService.class);
        // userService.sayHi(any(String.class));
        // 对无返回值的方法 mock(UserService.class);后内部执行逻辑会被空调用覆盖.
        Mockito.doNothing().when(userService).sayHi(any(String.class));
        // 有返回值的方法
        when(userService.sayHello(any(String.class))).thenReturn("mock sayHello!");

        // 设置业务服务.
        UserAction userAction = new UserAction();
        userAction.setUserService(userService);

        // 执行目标业务方法.
        userAction.executeForPublic("public");

        // 执行校验.
        verify(userService, times(1)).sayHello(any(String.class));
        verify(userService, times(1)).sayHi(any(String.class));
    }

}
