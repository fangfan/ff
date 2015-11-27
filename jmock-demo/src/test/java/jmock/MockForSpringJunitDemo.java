package jmock;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wit.service.UserAction;
import org.wit.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MockForSpringJunitDemo {

    public Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private UserService mockService = context.mock(UserService.class);

    @Autowired
    private UserAction userAction;

    @Before
    public void init() {
        // 设置期望。
        context.checking(new Expectations() {
            {
                allowing(mockService).sayHello(with(any(String.class)));
                
                will(returnValue("mock"));
            }
        });
        userAction.setUserService(mockService);
    }

    @DirtiesContext
    @Test
    public void demo() throws Exception {
        System.out.println(userAction.getClass().getClassLoader());
        userAction.execute("real");
        context.assertIsSatisfied();
    }

}
