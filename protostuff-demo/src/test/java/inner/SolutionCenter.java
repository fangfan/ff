package inner;

/**
 * Created by F.Fang on 2015/12/8.
 */
public class SolutionCenter {

    public static ISolution getAddSolution(){
        return new ISolution() {
            @Override
            public String solute(String exp) {
                return "add "+ exp;
            }
        };
    }

    public static ISolution getSubtractSolution(){
        return new ISolution() {
            @Override
            public String solute(String exp) {
                return "subtract "+exp;
            }
        };
    }

    public static ISolution getMutiplySolution(){
        return new ISolution() {
            @Override
            public String solute(String exp) {
                return "mutiply "+exp;
            }
        };
    }

    public static ISolution getDivideSolution(){
        return new ISolution() {
            @Override
            public String solute(String exp) {
                return "divide "+exp;
            }
        };
    }

}
