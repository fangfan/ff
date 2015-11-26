package com.adchina.dbutil;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration( "classpath:applicationContext-jdbc.xml" )
public class AggregateFunctionTest extends AbstractJUnit4SpringContextTests{
	
	@Test
	public void testCount(){
		String sql = "select count(id) from audience";
		System.out.println(CustomDataHandlerContext.getInstance().query(sql, null));
	}
	
	@Test
	public void testSum(){
		String sql = "select sum(pay) from audience";
		System.out.println(CustomDataHandlerContext.getInstance().query(sql, null));
	}


}
