package jmockit;

import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Test;
import org.wit.service.StaticUserService;
import org.wit.service.UserAction;

public class MockForPublicStaticDemo {

    @Test
    public void demo() {
        new NonStrictExpectations(StaticUserService.class) {
            {
                StaticUserService.sayHello(anyString);
                result = "mock";
            }
        };

        // assertEquals("mock", StaticUserService.sayHello("real"));
        UserAction userAction = new UserAction();
        userAction.executeForPublicStatic1("real");

        new Verifications() {
            {
                StaticUserService.sayHello(anyString);
                times = 1;
            }
        };
    }

}
