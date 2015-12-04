package org.wit.fddl.datahandle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wit.fddl.exception.NestedComponentException;

/**
 * 将List<Map<String,Object>> 转换成对象列表.
 * 
 * @author F.Fang
 * 
 */
public final class DataHandleUtil {
	private static final Log LOG = LogFactory.getLog(DataHandleUtil.class);

	@SuppressWarnings("rawtypes")
	private static Map<Class, Class> bigDecimalMappingTypes = new HashMap<Class, Class>();

	@SuppressWarnings("rawtypes")
	private static Map<Class, Class> bigIntegerMappingTypes = new HashMap<Class, Class>();

	@SuppressWarnings("rawtypes")
	private static Map<Class, Class> dateMappingTypes = new HashMap<Class, Class>();

	static {

		// BigDemical可以转换为普通的int,long,double,float,byte

		bigDecimalMappingTypes.put(int.class, java.math.BigDecimal.class);
		bigDecimalMappingTypes.put(long.class, java.math.BigDecimal.class);
		bigDecimalMappingTypes.put(short.class, java.math.BigDecimal.class);
		bigDecimalMappingTypes.put(double.class, java.math.BigDecimal.class);
		bigDecimalMappingTypes.put(float.class, java.math.BigDecimal.class);
		bigDecimalMappingTypes.put(byte.class, java.math.BigDecimal.class);
		bigDecimalMappingTypes.put(java.lang.Integer.class, java.math.BigDecimal.class);
		bigDecimalMappingTypes.put(java.lang.Long.class, java.math.BigDecimal.class);
		bigDecimalMappingTypes.put(java.lang.Short.class, java.math.BigDecimal.class);
		bigDecimalMappingTypes.put(java.lang.Byte.class, java.math.BigDecimal.class);
		bigDecimalMappingTypes.put(java.lang.Double.class, java.math.BigDecimal.class);
		bigDecimalMappingTypes.put(java.lang.Float.class, java.math.BigDecimal.class);

		// java.sql.TimeStap可以转换为普通的Date
		dateMappingTypes.put(java.util.Date.class, java.sql.Timestamp.class);

		bigIntegerMappingTypes.put(int.class, java.math.BigInteger.class);
		bigIntegerMappingTypes.put(long.class, java.math.BigInteger.class);
		bigIntegerMappingTypes.put(short.class, java.math.BigInteger.class);
		bigIntegerMappingTypes.put(double.class, java.math.BigInteger.class);
		bigIntegerMappingTypes.put(float.class, java.math.BigInteger.class);
		bigIntegerMappingTypes.put(byte.class, java.math.BigInteger.class);
		bigIntegerMappingTypes.put(java.lang.Integer.class, java.math.BigInteger.class);
		bigIntegerMappingTypes.put(java.lang.Long.class, java.math.BigInteger.class);
		bigIntegerMappingTypes.put(java.lang.Short.class, java.math.BigInteger.class);
		bigIntegerMappingTypes.put(java.lang.Byte.class, java.math.BigInteger.class);
		bigIntegerMappingTypes.put(java.lang.Double.class, java.math.BigInteger.class);
		bigIntegerMappingTypes.put(java.lang.Float.class, java.math.BigInteger.class);
	}

	/**
	 * 禁止实例化.
	 */
	private DataHandleUtil() {
	}

	/**
	 * 
	 * @param sourceData
	 *            源数据.
	 * @param targetClass
	 *            目标实体类型
	 * @return 目标实体类型实例对象列表.
	 */
	public static <T> List<T> mapToList(List<Map<String, Object>> sourceData, Class<T> targetClass) {
		if (sourceData == null || sourceData.isEmpty()) {
			return new ArrayList<T>();
		}
		List<T> result = new ArrayList<T>(sourceData.size());
		for (Map<String, Object> record : sourceData) {
			try {
				T target = targetClass.newInstance();

				for (Entry<String, Object> entry : record.entrySet()) {
					// 获取字段.
					Field entityField = FieldUtils.getField(targetClass, entry.getKey(), true);

					if (entityField != null) {
						try {
							invokeTargetField(targetClass, target, entry, entityField);
						} catch (Exception e) {
							Object entryValue = entry.getValue();
							Class<?> entryValueClass = null;
							if (entryValue != null) {
								entryValueClass = entryValue.getClass();
							}
							LOG.error("填充目标类[" + targetClass + "]属性值发生异常,属性类型[" + entityField.getType() + "]属性名称["
											+ entityField.getName() + "],源数据属性名称[" + entry.getKey() + "]类型为["
											+ entryValueClass + "]", e);
						}
					} else {
						LOG.warn("目标类[" + targetClass + "]无属性[" + entry.getKey() + "]!");
					}
				}
				result.add(target);
			} catch (Exception e) {
				throw new NestedComponentException("构造目标类型(" + targetClass + ")实例或赋值实例数据发生异常!", e);
			}
		}
		return result;
	}

	/**
	 * 只需要将源数据字段对应与实体属性对应中不匹配的字段填充,字段名一致的会正常执行.
	 * 
	 * @param sourceData
	 *            源数据.
	 * @param targetClass
	 *            目标实体类型
	 * @param fieldMap
	 *            源数据字段对应与实体属性对应
	 * @return 目标实体类型实例对象列表.
	 */
	public static <T> List<T> mapToList(List<Map<String, Object>> sourceData, Class<T> targetClass,
					Map<String, String> fieldMap) {
		if (sourceData == null || sourceData.isEmpty()) {
			return new ArrayList<T>();
		}
		List<T> result = new ArrayList<T>(sourceData.size());
		for (Map<String, Object> record : sourceData) {
			try {
				T target = targetClass.newInstance();
				for (Entry<String, Object> entry : record.entrySet()) {
					String dataFieldName = entry.getKey();
					String entityFieldName = dataFieldName;
					if (fieldMap.containsKey(dataFieldName)) {
						entityFieldName = fieldMap.get(dataFieldName);
					}
					// 获取字段.
					Field entityField = FieldUtils.getField(targetClass, entityFieldName, true);
					if (entityField != null) {
						try {
							invokeTargetField(targetClass, target, entry, entityField);
						} catch (Exception e) {
							Object entryValue = entry.getValue();
							Class<?> entryValueClass = null;
							if (entryValue != null) {
								entryValueClass = entryValue.getClass();
							}
							LOG.error("填充目标类[" + targetClass + "]属性值发生异常,属性类型[" + entityField.getType() + "]属性名称["
											+ entityFieldName + "],源数据属性名称[" + dataFieldName + "]类型为["
											+ entryValueClass + "]", e);
						}
					} else {
						LOG.warn("目标类[" + targetClass + "]无属性[" + entityFieldName + "]!");
					}
				}
				result.add(target);
			} catch (Exception e) {
				throw new NestedComponentException("构造目标类型(" + targetClass + ")实例发生异常!", e);
			}
		}
		return result;
	}

	private static <T> void invokeTargetField(Class<T> targetClass, T target, Entry<String, Object> entry,
					Field entityField) throws IllegalAccessException, InvocationTargetException {
		// 依据字段获取方法.
		Object entryValue = entry.getValue();
		if (entryValue != null) {
			Class<?> entryValueClass = entryValue.getClass();
			Method targetSetMethod = getSetterMethod(targetClass, entityField);
			if (targetSetMethod != null) {
				// 如果是BigDecimal 和基本数据类型的转换.
				if (entryValueClass.equals(java.math.BigDecimal.class)
								&& bigDecimalMappingTypes.containsKey(entityField.getType())) {
					BigDecimal source = (BigDecimal) entry.getValue();
					targetSetMethod.invoke(target, CommonsDataTranslate.translateNumber(source, entityField.getType()));
				} else if (entryValueClass.equals(java.sql.Timestamp.class)
								&& dateMappingTypes.containsKey(entityField.getType())) {
					// 如果是java.sql.Timestamp 和 java.util.Date的转换.
					java.sql.Timestamp date = (java.sql.Timestamp) entry.getValue();
					targetSetMethod.invoke(target, CommonsDataTranslate.translateUtilDate(date));
				} else if (entryValueClass.equals(java.math.BigInteger.class)
								&& bigIntegerMappingTypes.containsKey(entityField.getType())) {
					//如果是BigInteger和基本数据类型的转换.
					BigInteger source = (BigInteger) entry.getValue();
					targetSetMethod.invoke(target, CommonsDataTranslate.translateNumber(source, entityField.getType()));
				} else {
					//其它
					targetSetMethod.invoke(target, entry.getValue());
				}
			}
		}

	}

	/**
	 * 依据字段获取字段的set方法.
	 * 
	 * @param targetClass
	 *            目标实体类
	 * @param entityField
	 *            目标字段.
	 * @return
	 */
	private static <T> Method getSetterMethod(Class<T> targetClass, Field entityField) {
		String fieldName = entityField.getName();
		String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase()
						+ fieldName.substring(1, fieldName.length());
		Method targetSetMethod = null;
		try {
			// 再获取目标方法.
			targetSetMethod = targetClass.getMethod(setterMethodName, entityField.getType());
		} catch (NoSuchMethodException e) {
			// 不做任何处理.
			LOG.error("目标类[" + targetClass + "]不存在set方法[" + setterMethodName + "],参数类型[" + entityField.getType() + "]",
							e);
		} catch (SecurityException e) {
			// 不做任何处理.
		}
		return targetSetMethod;
	}

}
