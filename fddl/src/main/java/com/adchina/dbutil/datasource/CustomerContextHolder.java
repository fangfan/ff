package com.adchina.dbutil.datasource;

public class CustomerContextHolder {
	/**
	 * 启用线程本地存储避免不同线程之间切换DBTag造成的冲突.
	 */
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDbTag(String dbTag) {
		contextHolder.set(dbTag);
	}

	public static String getDbTag() {
		return (contextHolder.get());
	}

	public static void clearDbTag() {
		contextHolder.remove();
	}
}
