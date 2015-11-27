package jmock;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.wit.service.UserAction;
import org.wit.service.UserService;

public class HelloWorldDemo {
    
    @Test
    public void demo(){
        // 建立一个test上下文对象。   
        Mockery context = new Mockery() {{  
            setImposteriser(ClassImposteriser.INSTANCE);
        }};     
      
        System.out.println(Thread.currentThread().getContextClassLoader());
        // 生成一个mock对象   
        final UserService mockService = context.mock(UserService.class); 
      
        // 设置期望。   
        context.checking(new Expectations() {   
            {   
                oneOf(mockService).sayHello(with(any(String.class)));   
                will(returnValue("mock"));   
            }   
        });   
      
        UserAction action = new UserAction();   
      
        // 设置mock对象   
        action.setUserService(mockService);
      
        // 调用方法   
        action.execute("real");
      
          
    }

}
