import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.junit.Test;

/**
 * Created by F.Fang on 2015/10/13.
 * Version :2015/10/13
 */
public class Base64Test {

    @Test
    public void demo() throws Exception {
        String str = "MjQ0MDAy";
        System.out.println(new String(Base64.decode(str)));
    }

}
