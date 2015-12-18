package inner;

import model.Solutions;
import org.junit.Test;
import org.wit.ff.serializer.ProtoStuffSerializer;

/**
 * Created by F.Fang on 2015/12/8.
 */
public class InnerTest {

    /**
     * 验证内部类的异常,为啥得到的异常不是栈的异常呢？难道记错了.
     */
    @Test
    public void demo(){
        Solutions solutions = new Solutions();
        solutions.addSolution("add",SolutionCenter.getAddSolution());
        solutions.addSolution("divide", SolutionCenter.getDivideSolution());

        ProtoStuffSerializer serializer = new ProtoStuffSerializer();
        serializer.serialize(solutions);
    }
}
