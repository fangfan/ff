package com.adchina.dmp.ndal.mongodb.op;

import org.junit.Test;

/**
 * Created by F.Fang on 2015/3/17.
 * Version :2015/3/17
 */
public class MathOperatorTest {

    @Test
    public void demo(){
        System.out.println(MathOperator.parse("!="));
        System.out.println(MathOperator.parse(">"));
        System.out.println(MathOperator.parse("$ne"));
    }
}
