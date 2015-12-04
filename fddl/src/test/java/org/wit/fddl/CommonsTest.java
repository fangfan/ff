package org.wit.fddl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import org.wit.fddl.datahandle.DataHandleUtil;
import org.wit.fddl.sql.SeperatorSymbol;
import org.wit.fddl.sql.SqlKeyWords;

public class CommonsTest {

	@Test
	public void testCopyProperties(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", 111);
		map.put("name", "ff");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		List<Person> result = DataHandleUtil.mapToList(list, Person.class);
		assertEquals("Person [id=111, name=ff]",StringUtils.join(result, ","));
	}
	
	@Test
	public void testCopyPropertiesForFieldMap(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id1", 111);
		map.put("name", "ff");
		Map<String,String> fieldMap = new HashMap<String,String>();
		fieldMap.put("id1", "id");
		//fieldMap.put("name1", "name");
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		List<Person> result = DataHandleUtil.mapToList(list, Person.class,fieldMap);
		
		assertEquals("Person [id=111, name=ff]",StringUtils.join(result, ","));
	}
	
	@Test
	public void testStringBuffer(){
	    
	    String upperField = "password as pwd".toUpperCase();
	    int asIndex = upperField.indexOf(SqlKeyWords.AS);
	    System.out.println(asIndex);
	    while(asIndex!=-1){
	        boolean result = upperField.charAt(asIndex-1) == SeperatorSymbol.EMPTY.charAt(0)
	        && upperField.charAt(asIndex + 2) == SeperatorSymbol.EMPTY.charAt(0);
	        System.out.println(result);
	        if(result){
	            break;
	        }
	        asIndex+=2;
	        if(asIndex>upperField.length()){
	            asIndex = upperField.length();
	        }
	        asIndex = upperField.indexOf(SqlKeyWords.AS,asIndex);
	    }
	}
	
	@Test
	public void testTimeStap(){
		Map<Integer,Long> map = new HashMap<Integer,Long>();
		map.put(1, 0L);
		map.put(2, 3L);
		for(Entry<Integer,Long> entry: map.entrySet()){
		    if(entry.getValue()==0L){
		        continue;
		    }
		    System.out.println(entry.getValue());
		}
	}
	
	public static class Person{
		int id;
		String name;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return "Person [id=" + id + ", name=" + name + "]";
		}
		
	}
}
