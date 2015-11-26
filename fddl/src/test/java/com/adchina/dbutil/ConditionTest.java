package com.adchina.dbutil;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.adchina.dbutil.condition.Condition;
import com.adchina.dbutil.sql.OrderByType;

/**
 * 测试条件构造器.
 * 
 * @author F.Fang
 * 
 */
public class ConditionTest {

	@Test
	public void testBuild() {
		Condition condition = new Condition.Builder().max("password").groupBy("name").orderBy("id", OrderByType.DESC)
						.build();

		assertEquals(1, condition.getMaxSettings().size());
		assertEquals(1, condition.getGroupBySettings().size());
		assertEquals(OrderByType.DESC, condition.getOrderBySettings().get("id"));
	}

}
