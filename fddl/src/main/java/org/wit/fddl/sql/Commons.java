package org.wit.fddl.sql;

public final class Commons {
	/**
	 * 私有构造函数,避免工具类被对象化.
	 */
	private Commons() {
	}

	public static String maxWrapper(String key) {
		return AggregationType.MAX.name() + SeperatorSymbol.LEFT_PARENTHESIS + key + SeperatorSymbol.RIGHT_PARENTHESIS;
	}

	public static String minWrapper(String key) {
		return AggregationType.MIN.name() + SeperatorSymbol.LEFT_PARENTHESIS + key + SeperatorSymbol.RIGHT_PARENTHESIS;
	}

	public static String countWrapper(String key) {
		return AggregationType.COUNT.name() + SeperatorSymbol.LEFT_PARENTHESIS + key
						+ SeperatorSymbol.RIGHT_PARENTHESIS;
	}

	public static String sumWrapper(String key) {
		return AggregationType.SUM.name() + SeperatorSymbol.LEFT_PARENTHESIS + key + SeperatorSymbol.RIGHT_PARENTHESIS;
	}

}
