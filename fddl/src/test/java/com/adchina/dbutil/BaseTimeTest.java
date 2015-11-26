package com.adchina.dbutil;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class BaseTimeTest {
	
	@Test
	public void testMs(){
		//以ms为单位.
		long time = 138000;
		System.out.println(time);
		//Date date = new Date(time);
		Calendar cl = Calendar.getInstance();
		cl.set(Calendar.HOUR_OF_DAY, 0);
		cl.set(Calendar.MINUTE, 0);
		cl.set(Calendar.SECOND, 0);
		Date date = new Date(cl.getTimeInMillis()+time);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		assertEquals("00:02:18",sdf.format(date));
	}
	
	@Test
	public void testDate(){
	    Calendar now = Calendar.getInstance();
	    Calendar lv = Calendar.getInstance();
	    lv.set(Calendar.HOUR_OF_DAY, 12);
	    long cap = (now.getTime().getTime()-lv.getTime().getTime());
	    int i = (int) ((now.getTime().getTime()-lv.getTime().getTime())/(1000*60*60));
	    System.out.println(cap+"||"+cap/(1000*60*60)+"||"+i);
	}
	
	@Test
	public void testArray(){
	    String[] s = {"11","3","1","2"};
	    
	    Arrays.sort(s);
	    System.out.println(Arrays.toString(s));
	}

}
