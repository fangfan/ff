package mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.wit.service.StaticUserService;
import org.wit.service.ConstructorService;
import org.wit.service.UserAction;
import org.wit.service.UserService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UserService.class,StaticUserService.class,ConstructorService.class,UserAction.class})

public class MockUserActionTest {
    
    @Test
    public void testMockForPublic(){
        UserService userService = mock(UserService.class);
        //userService.sayHi(any(String.class));
        //对无返回值的方法 mock(UserService.class);后内部执行逻辑会被空调用覆盖.
        Mockito.doNothing().when(userService).sayHi(any(String.class));
        //有返回值的方法
        when(userService.sayHello(any(String.class))).thenReturn("mock sayHello!");
        
        UserAction userAction = new UserAction();
        userAction.setUserService(userService);
        
        userAction.executeForPublic("public");
        
        verify(userService, times(1)).sayHello(any(String.class));
        verify(userService, times(1)).sayHi(any(String.class));
    }
    
    @Test
    public void testMockForReal(){
        UserService userService = mock(UserService.class, Mockito.CALLS_REAL_METHODS);
        //UserService userService = mock(UserService.class);
        UserAction userAction = new UserAction();
        userAction.setUserService(userService);
        userAction.executeForReal("real");
        
    }
    
    @Test
    public void testMockForPrivate() throws Exception{
        UserService userService = PowerMockito.spy(new UserService());
        
        //模拟返回值私有方法.
        //PowerMockito.when(userService, "secreteSayHello", any(String.class)).thenReturn("mock");
        PowerMockito.doReturn("mock").when(userService, "secreteSayHello", any(String.class));
        //模拟私有空方法.
        PowerMockito.doNothing().when(userService, "secreteSayHi", any(String.class));
        UserAction userAction = new UserAction();
        userAction.setUserService(userService);
        userAction.executeForPrivate("private");
        PowerMockito.verifyPrivate(userService,Mockito.times(1)).invoke("secreteSayHello", any(String.class));
        PowerMockito.verifyPrivate(userService,Mockito.times(1)).invoke("secreteSayHi", any(String.class));
    }
    
    @Test
    public void testMockForPublicStatic() throws Exception {
        //mock会模拟所有的方法.
        //PowerMockito.mock(StaticUserService.class);
        //spy只会模拟指定模拟行为的方法.
        PowerMockito.spy(StaticUserService.class);
        //模拟返回值的public T static 方法.
        when(StaticUserService.sayHello(any(String.class))).thenReturn("mock");
        //模拟无返回值的public void static 方法
        PowerMockito.doNothing().when(StaticUserService.class, "sayHi", anyString());
        
        UserAction userAction = new UserAction();
        userAction.executeForPublicStatic("public static");
        
        PowerMockito.verifyStatic(Mockito.times(1));
        StaticUserService.sayHello(anyString());
        
        PowerMockito.verifyStatic(Mockito.times(1));
        StaticUserService.sayHi(anyString());
        
        //StaticUserService.secreteSay("real thing!");
        
    }
    
    @Test
    public void testMockForPrivateStatic() throws Exception {
        PowerMockito.spy(StaticUserService.class);
        //模拟返回值私有方法.
        //PowerMockito.when(userService, "secreteSayHello", any(String.class)).thenReturn("mock");
        PowerMockito.doReturn("mock").when(StaticUserService.class, "secreteSayHello", any(String.class));
        //模拟私有空方法.
        PowerMockito.doNothing().when(StaticUserService.class, "secreteSayHi", any(String.class));
        
        UserAction userAction = new UserAction();
        userAction.executeForPrivateStatic("fj");
        userAction.executeForPrivateStatic("fj");
        
        PowerMockito.verifyPrivate(StaticUserService.class,Mockito.times(2)).invoke("secreteSayHello", any(String.class));
        PowerMockito.verifyPrivate(StaticUserService.class,Mockito.times(2)).invoke("secreteSayHi", any(String.class));
    }
    
    @Test
    public void testMockForConstructor() throws Exception {
        UserAction userAction = new UserAction();
        //模拟构造函数.
        //value必须提前构造.
        ConstructorService value = new ConstructorService("mock");
        PowerMockito.spy(ConstructorService.class);
        //模拟构造函数.
        PowerMockito.whenNew(ConstructorService.class).withArguments(anyString()).thenReturn(value);
        //执行业务逻辑.
        userAction.executeForConstructor("real");
        //验证构造方法.
        PowerMockito.verifyNew(ConstructorService.class, times(1)).withArguments(anyString());
    }
    
    @Test
    public void testMockForFinal() throws Exception {
        UserService userService = PowerMockito.spy(new UserService());
        when(userService.sayFinal("final")).thenReturn("mock");
        
        UserAction userAction = new UserAction();
        userAction.setUserService(userService);
        userAction.executeForFinal("final");
        
        verify(userService,times(1)).sayFinal("final");
    }

}
