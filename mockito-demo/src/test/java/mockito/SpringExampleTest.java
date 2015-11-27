package mockito;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import powermock.examples.spring.IdGenerator;
import powermock.examples.spring.Message;
import powermock.examples.spring.MyBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/example-context.xml")
@PrepareForTest(IdGenerator.class)
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
                "javax.management.*" })
public class SpringExampleTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Autowired
    private MyBean myBean;

    @Test
    public void mockStaticMethod() throws Exception {
        // Given
        final long expectedId = 2L;
        PowerMockito.mockStatic(IdGenerator.class);
        PowerMockito.when(IdGenerator.generateNewId()).thenReturn(expectedId);

        // When
        final Message message = myBean.generateMessage();

        // Then
        assertEquals(expectedId, message.getId());
        assertEquals("My bean message", message.getContent());
    }
}