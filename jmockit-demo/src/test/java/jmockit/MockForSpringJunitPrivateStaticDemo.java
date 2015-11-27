package jmockit;

import static mockit.Deencapsulation.invoke;
import mockit.Expectations;
import mockit.Verifications;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wit.service.StaticUserService;
import org.wit.service.UserAction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MockForSpringJunitPrivateStaticDemo {

    @Autowired
    private UserAction userAction;

    @Test
    public void demo() throws Exception {

        new Expectations(StaticUserService.class) {
            {
                invoke(StaticUserService.class, "secreteSayHi", anyString);
                invoke(StaticUserService.class, "secreteSayHello", anyString);
                result = "mock";
            }

        };

        userAction.executeForPrivateStatic("real");

        new Verifications() {
            {
                invoke(StaticUserService.class, "secreteSayHi", anyString);
                times = 1;
                invoke(StaticUserService.class, "secreteSayHello", anyString);
                times = 1;
            }
        };
    }

}
