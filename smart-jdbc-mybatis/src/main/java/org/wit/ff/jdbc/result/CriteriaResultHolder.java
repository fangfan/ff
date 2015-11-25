package org.wit.ff.jdbc.result;

/**
 * Created by F.Fang on 2015/11/19.
 */
public class CriteriaResultHolder {

    private static final ThreadLocal<CriteriaResult> criteriaResult = new ThreadLocal<CriteriaResult>();

    private CriteriaResultHolder(){}

    public static CriteriaResult get() {
        return criteriaResult.get();
    }

    public static void set(CriteriaResult value){
        if(criteriaResult.get() == null && value!=null){
            criteriaResult.set(value);
        }
    }

    public static void remove(){
        criteriaResult.remove();
    }
}
