package com.adchina.dmp.ndal.mongodb.cri;

import com.adchina.dmp.ndal.mongodb.cri.Criteria;
import com.adchina.dmp.ndal.mongodb.cri.CriteriaChain;
import com.adchina.dmp.ndal.mongodb.cri.CriteriaGroup;
import com.adchina.dmp.ndal.mongodb.op.MathOperator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by F.Fang on 2015/3/4.
 * Version :2015/3/4
 */
public class CriteriaChainTest {

    @Test
    public void demo(){
        CriteriaChain cc = new CriteriaChain();

        CriteriaGroup group1 = new CriteriaGroup()
                .append(new Criteria("age").gt())
                .append(new Criteria("age").lt())
                .AND();

        CriteriaGroup group2 = new CriteriaGroup().
                append(new String[]{"age","age"},
                        new MathOperator[]{MathOperator.GT, MathOperator.LT})
                .AND();

        assertEquals(group1.build(), group2.build());

        cc.append(group1);

        System.out.println(cc.build());
    }
}
