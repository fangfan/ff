import org.junit.Test;
import org.wit.ff.util.ByteUtil;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;


/**
 * Created by F.Fang on 2015/12/3.
 */
public class ByteTest {

    @Test
    public void demo(){
        int i = 128;
        byte[] arr = ByteUtil.intToByte4(i);
        System.out.println(Arrays.toString(arr));
        int result = ByteUtil.byte4ToInt(arr);
        System.out.println(result);
        assertEquals(i, result);

    }

}
