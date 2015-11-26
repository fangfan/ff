package com.adchina.dbutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class BaseSortTest {
	
	@Test
	public void testDefault(){
		String[] s = new String[]{"a1","a3","a2"};
		Arrays.sort(s);
		assertEquals("a1:a2:a3",StringUtils.join(s, ":"));
		
		Object[] arr = new Object[]{1,2,3};
		assertTrue(arr[0] instanceof Integer);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("doubleid",1.3);
		map.put("intid", 1);
		map.put("longid", 11111111111111111L);
		map.put("floatid", 1.23f);
		
		//map或数据库结果集合返回的记录一般都是包装类型.
		assertEquals(java.lang.Double.class,map.get("doubleid").getClass());
		assertEquals(java.lang.Integer.class,map.get("intid").getClass());
		assertEquals(java.lang.Long.class,map.get("longid").getClass());
		assertEquals(java.lang.Float.class,map.get("floatid").getClass());
		
		int[] intArr = new int[]{1,10,9,8,7,6};
		Arrays.sort(intArr, 1, 6);
		assertEquals("[1, 6, 7, 8, 9, 10]",Arrays.toString(intArr));
	}

}
