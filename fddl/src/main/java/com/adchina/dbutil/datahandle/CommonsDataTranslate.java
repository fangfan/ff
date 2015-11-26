package com.adchina.dbutil.datahandle;

import java.util.Date;

/**
 * 处理基本数据转换.
 * @author F.Fang
 *
 */
public final class CommonsDataTranslate {
	
	private CommonsDataTranslate(){}
	
	public static Object translateNumber(Number source, Class targetType){
		if(targetType.equals(int.class)||targetType.equals(java.lang.Integer.class)){
			return source.intValue();
		}else if(targetType.equals(long.class)||targetType.equals(java.lang.Long.class)){
			return source.longValue();
		}else if(targetType.equals(byte.class)||targetType.equals(java.lang.Byte.class)){
			return source.byteValue();
		}else if(targetType.equals(double.class)||targetType.equals(java.lang.Double.class)){
			return source.doubleValue();
		}else if(targetType.equals(float.class)||targetType.equals(java.lang.Float.class)){
			return source.floatValue();
		}else{
			return source;
		}
	}

	
	public static Object translateUtilDate(java.sql.Timestamp source){
		Date date = new Date(source.getTime());
		return date;
	}

}
