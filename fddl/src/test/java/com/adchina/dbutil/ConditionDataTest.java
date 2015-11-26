package com.adchina.dbutil;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ConditionDataTest {
	
	@Test
	public void testDistinct(){
		List<Map<String,Object>> list  = new ArrayList<Map<String,Object>>();
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("id", 10);
		map1.put("name", "ff");
		list.add(map1);
		
		Map<String,Object> map2 = new HashMap<String,Object>();
		map2.put("id", 10);
		list.add(map2);
		
		Map<String,Object> map3 = new HashMap<String,Object>();
		map3.put("id", 10);
		list.add(map3);
		
		assertEquals(3,list.size());
		assertEquals(2,new HashSet<Map<String,Object>>(list).size());
	}

}
