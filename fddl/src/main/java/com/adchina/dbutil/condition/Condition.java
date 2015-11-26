package com.adchina.dbutil.condition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.adchina.dbutil.sql.OrderByType;
import com.adchina.dbutil.sql.keys.SelectSqlKey;

/**
 * 数据合并条件(各db数据汇总)
 * @author F.Fang
 *
 */
public class Condition {
	/**
	 * 去重复键设置.
	 * 默认情况下去掉相同对象(比如以主键id为可选项).
	 */
	private final boolean distinct;
	
	/**
	 * 最大值.
	 */
	private final List<String> maxSettings;
	
	/**
	 * 最小值.
	 */
	private final List<String> minSettings;
	
	/**
	 * 合计.
	 */
	private final List<String> sumSettings;
	
	/**
	 * 统计.
	 */
	private final List<String> countSettings;
	
	/**
	 * 分组键设置.
	 */
	private final List<String> groupBySettings;
	
	/**
	 * 排序列.
	 */
	private final Map<String,OrderByType> orderBySettings ;
	
	/**
	 * 排序列字段.
	 */
	private final List<String> orderByFields;
	
	/**
	 * 分页.
	 */
	private final int[] pagingSettings;
	
	private final boolean pagingFlag;
	
	private final List<SelectSqlKey> selectSqlKeys;
	
	private Condition(Builder builder){
		distinct = builder.distinct;
		maxSettings = builder.maxSettings;
		minSettings = builder.minSettings;
		countSettings = builder.countSettings;
		sumSettings = builder.sumSettings;
		groupBySettings = builder.groupBySettings;
		orderBySettings = builder.orderBySettings;
		pagingSettings = builder.pagingSettings;
		pagingFlag = builder.pagingFlag;
		orderByFields = builder.orderByFields;
		selectSqlKeys = builder.selectSqlKeys;
	}

	public static class Builder {
		
		/**
		 * 分组设置.
		 */
		private List<String> groupBySettings = new ArrayList<String>();
		
		/**
		 * 去重复设置.
		 * 默认情况下去掉相同对象(比如以主键id为可选项).
		 */
		private boolean distinct;
		
		/**
		 * 排序列,请注意使用LinkedHashMap而不是普通的HashMap以保证数据的插入顺序和用户插入的顺序一致.
		 */
		private Map<String,OrderByType> orderBySettings = new LinkedHashMap<String,OrderByType>();
		
		/**
		 * 排序列字段.
		 */
		private List<String> orderByFields = new ArrayList<String>();
		
		/**
		 * 最大值.
		 */
		private List<String> maxSettings = new ArrayList<String>();
		
		/**
		 * 最小值.
		 */
		private List<String> minSettings = new ArrayList<String>();
		
		/**
		 * 合计.
		 */
		private List<String> sumSettings = new ArrayList<String>();
		
		/**
		 * 统计.
		 */
		private List<String> countSettings = new ArrayList<String>();
		
		/**
		 * 分页.
		 */
		private int[] pagingSettings = new int[2];
		
		/**
		 * 启用分页.
		 */
		private boolean pagingFlag;
		
		private List<SelectSqlKey> selectSqlKeys;
		
		/**
		 * 去重过滤.
		 * @return
		 */
		public Builder distinct(){
			distinct = true;
			return this;
		}
		
		/**
		 * 最大值设置.
		 * @param key 最大值键.
		 * @return
		 */
		public Builder max(String key){
			maxSettings.add(key);
			return this;
		}
		
		/**
		 * 最小值设置.
		 * @param key 最小值键.
		 * @return
		 */
		public Builder min(String key){
			minSettings.add(key);
			return this;
		}
		
		/**
		 * 统计设置.
		 * @param key 统计键.
		 * @return
		 */
		public Builder count(String key){
			countSettings.add(key);
			return this;
		}
		
		/**
		 * 求和设置.
		 * @param key 求和键.
		 * @return
		 */
		public Builder sum(String key){
			sumSettings.add(key);
			return this;
		}
		

		/**
		 * 增加分组条件.
		 * @param key 分组条件.
		 * @return 条件构造器.
		 */
		public Builder groupBy(String key){
			groupBySettings.add(key);
			return this;
		};
		
		/**
		 * 添加排序键.
		 * @param key
		 * @return
		 */
		public Builder orderBy(String key, OrderByType type){
			orderByFields.add(key);
			orderBySettings.put(key,type);
			return this;
		}
		
		/**
		 * 分页设置.
		 * @param startIndex 起始索引.
		 * @param offSet 偏移量.
		 * @return
		 */
		public Builder paging(int startIndex, int offSet){
			pagingSettings[0] = startIndex;
			pagingSettings[1] = offSet;
			pagingFlag = true;
			return this;
		}
		
		/**
		 * 填充选择字段.
		 * @param newSelectSqlKeys
		 * @return
		 */
		public Builder fillSelectSqlKeys(List<SelectSqlKey> newSelectSqlKeys){
		    selectSqlKeys = newSelectSqlKeys;
		    return this;
		}
		
		/**
		 * 构造外部条件.
		 * @return 外部条件对象.
		 */
		public Condition build(){
			return new Condition(this);
		}
	}

	public boolean isDistinct() {
		return distinct;
	}

	public List<String> getMaxSettings() {
		return maxSettings;
	}

	public List<String> getMinSettings() {
		return minSettings;
	}

	public List<String> getSumSettings() {
		return sumSettings;
	}

	public List<String> getCountSettings() {
		return countSettings;
	}

	public List<String> getGroupBySettings() {
		return groupBySettings;
	}

	public Map<String,OrderByType> getOrderBySettings() {
		return orderBySettings;
	}

	public List<String> getOrderByFields() {
		return orderByFields;
	}

	public int[] getPagingSettings() {
		return pagingSettings;
	}

	public boolean isPagingFlag() {
		return pagingFlag;
	}
	
	public boolean isNoAggregateFunction() {
		return maxSettings.isEmpty() && minSettings.isEmpty() && countSettings.isEmpty() && sumSettings.isEmpty();
	}

    public List<SelectSqlKey> getSelectSqlKeys() {
        return selectSqlKeys;
    }

}
