package org.wit.fddl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.wit.fddl.datahandle.DataHandleUtil;
import org.wit.fddl.model.DeliveryDailyData;
import org.wit.fddl.model.SonComplexData;
import org.wit.fddl.model.WrapperData;

public class CanBeTranslateTypelTest {
	
	@Test
	public void testCanBeTranslate(){
		Map<String,Object> data = new HashMap<String,Object>();
		//data.put("id", new BigDecimal(1));
		data.put("UV", new java.math.BigInteger("100"));
		data.put("settled_time", new Timestamp(Calendar.getInstance().getTimeInMillis()));
		
		List<Map<String,Object>> sourceData = new ArrayList<Map<String,Object>>();
		sourceData.add(data);
		
		Map<String, String> dailyUvFieldMap = new HashMap<String, String>();
		dailyUvFieldMap.put("settled_time", "settledTime");
		dailyUvFieldMap.put("UV", "dailyUV");
		
		List<DeliveryDailyData> list = DataHandleUtil.mapToList(sourceData, DeliveryDailyData.class, dailyUvFieldMap);
		System.out.println(list);
	}
	
	@Test
	public void testWrapperTranslate(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("id", 1);
		data.put("age", 100);
		
		List<Map<String,Object>> sourceData = new ArrayList<Map<String,Object>>();
		sourceData.add(data);
		
		List<WrapperData> list = DataHandleUtil.mapToList(sourceData, WrapperData.class);
		System.out.println(list);
	}
	
	@Test
	public void testLatestMatchType(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("id", new BigDecimal(1));
		data.put("money", new java.math.BigInteger("100"));
		data.put("age", new java.math.BigInteger("20"));
		data.put("date", new Timestamp(Calendar.getInstance().getTimeInMillis()));
		
		List<Map<String,Object>> sourceData = new ArrayList<Map<String,Object>>();
		sourceData.add(data);
		
		List<SonComplexData> list = DataHandleUtil.mapToList(sourceData, SonComplexData.class);
		System.out.println(list);
	}

}


