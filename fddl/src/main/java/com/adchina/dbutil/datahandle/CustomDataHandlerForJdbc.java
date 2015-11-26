package com.adchina.dbutil.datahandle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.adchina.dbutil.condition.Condition;
import com.adchina.dbutil.exception.NestedComponentException;
import com.adchina.dbutil.sql.AggregationType;
import com.adchina.dbutil.sql.Commons;
import com.adchina.dbutil.sql.OrderByType;
import com.adchina.dbutil.sql.SeperatorSymbol;
import com.adchina.dbutil.sql.SqlKeyIndex;
import com.adchina.dbutil.sql.SqlTree;
import com.adchina.dbutil.sqlparser.QuerySqlParser;

/**
 * 使用SpringJdbc作为基础构造.
 * 
 * @author F.Fang
 * 
 */
public class CustomDataHandlerForJdbc extends CustomDataHandler {

	private static final Log LOG = LogFactory.getLog(CustomDataHandlerForJdbc.class);

	private JdbcTemplate jdbcTemplate;

	public CustomDataHandlerForJdbc(List<String> dbs, JdbcTemplate jdbcTemplate) {
		super(dbs);
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 请注意,如果数据库是单个db,那么只需要对语句的正确性做一部分验证 不需要解析条件,不需要构造执行语句
	 * 因为真实执行的sql就是外部传入的sql,不需要改变.
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public Object query(final String sql, final Object[] args) {

		// (1)解析sql,构造SqlTree
		SqlTree sqlTree = new QuerySqlParser(sql).parse();

		// (2)获取执行语句.
		String executeSql = getExecuteSql(sqlTree);
		LOG.info("执行的语句为:" + executeSql);

		// (3)定义执行器.
		DataExecutor executor = getExecutor(executeSql, args);

		// (4)调用基类方法执行数据库查询过程和数据的再合并.
		Map<String, DataModel> dataContainer = query(executor);

		// (5)执行目标条件,返回结果.
		// 目前条件为空,不执行条件过滤.
		// 最好返回数据收集器,可以重复利用已查询到的数据,求最大值,最小值都可以一次性拿到.
		// 执行排序和分组前 去掉无关排序列 去掉无关的分组列
		// 比如结果集合 count(id) 但是group by id,username,去掉username.
		// select count(id) from users order by id,username,
		// 执行完数据库sql后,排序结果集时username排序实际上也没有意义.
		return getTargetData(dataContainer, sqlTree.getCondition());
	}

	/**
	 * 获取执行语句. 如果是单个db直接返回原始语句. 否则原始sql需要去掉 order by , group by 分页等条件之后的语句.
	 * 
	 * @param sqlTree
	 * @return 具体数据库执行语句.
	 */
	private String getExecuteSql(SqlTree sqlTree) {
		// 如果是单个db直接返回原始语句
		// 否则原始sql需要去掉 order by , group by 分页等条件之后的语句.
		return isSingleDB() ? sqlTree.getOrignalSql() : sqlTree.getDbExecuteSql();
	}

	/**
	 * 构造执行器.
	 * 
	 * @param executeSql
	 *            执行sql.
	 * @param args
	 *            参数.
	 * @return
	 */
	private DataExecutor getExecutor(final String executeSql, final Object[] args) {
		return new DataExecutor() {
			@Override
			public JdbcDataModel execute() {
				// 接收任何sql语句的查询命令,均选择List<Map<String,Object>>作为结果集.
				JdbcDataModel dataModel = null;
				long start = System.nanoTime();
				try {
					List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(executeSql, args);
					long end = System.nanoTime();
					// 使用ms来计算时间.
					dataModel = new JdbcDataModel((end - start) / 1000000L);
					dataModel.setData(resultSet);
					dataModel.setCount(resultSet.size());
				} catch (Exception e) {
					// 发生异常时,直接全局抓取异常,保证线程任务内的异常可以被截获.
					long end = System.nanoTime();
					dataModel = new JdbcDataModel((end - start), e.getMessage());
				}
				return dataModel;
			}
		};
	}

	/**
	 * 此方法暂时不抽象.
	 * 
	 * @param dataContainer
	 * @param condition
	 * @return
	 */
	private Object getTargetData(Map<String, DataModel> dataContainer, Condition condition) {
		// 若数据集为空,则返回null.
		if (dataContainer.isEmpty()) {
			return null;
		}

		// (1) 构造sql查询的结果集合列表
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Entry<String, DataModel> entry : dataContainer.entrySet()) {
			JdbcDataModel model = (JdbcDataModel) entry.getValue();
			// 只要一个任务执行发生异常,那么整体将抛出异常表示任务失败.
			if (model.isErrorFlag()) {
				throw new NestedComponentException("DB(" + entry.getKey() + ")查询任务失败," + model.getMsg());
			} else {
				LOG.info("DB(" + entry.getKey() + ")执行语句结果{" + model.getSqlExecuteInfo() + "}");
			}
			list.addAll(model.getData());
		}
		// (2) 循环结果集合,检索目标数据.
		// 如果数据只是从一个DB获取或数据列表为空,那么之后的所有条件都不需要执行.
		if (isSingleDB() || list.isEmpty()) {
			return list;
		} else {
			// 如果结果集不是来自于一个DB,那么之后的条件都需要继续过滤与分组.
			return handleDataWithCondition(list, condition);
		}
	}

	/**
	 * 分组函数当中的列不允许出现对分组列的聚合.
	 * 
	 * @param list
	 * @param condition
	 * @return
	 */
	private List<Map<String, Object>> handleDataWithCondition(List<Map<String, Object>> list, Condition condition) {

		// 1,处理distinct,去重
		List<Map<String, Object>> fristStepResult = null;
		// 如果存在count统计那么不应该执行distinct???.
		if (condition.isDistinct()) {
			Set<Map<String, Object>> set = new HashSet<Map<String, Object>>(list);
			// distinct
			fristStepResult = new ArrayList<Map<String, Object>>(set);
		} else {
			fristStepResult = list;
		}

		// 2,处理group by & 聚合函数 & 排序.
		// 分组的字段必须要出现在选择的字段列表当中.

		List<Map<String, Object>> secondStepResult = null;
		// 分组聚集函数和排序均不存在,直接取第一步处理的结果.
		if (condition.getGroupBySettings().isEmpty() && condition.getOrderBySettings().isEmpty()&&condition.isNoAggregateFunction()) {
			secondStepResult = fristStepResult;
		} else {
			// 分组和排序必然存在一种
			// 分组与合并聚集函数.
			Object[] groupByAndaggregateArr = handleGroupByAndAggregateFunction(condition, fristStepResult);
			// 排序.
			List<Map<String, Object>> orderByList = handleOrderBy(condition, groupByAndaggregateArr);
			secondStepResult = orderByList;
		}

		// 如果执行了二级甚至多级排序,那么将排序后的数组转换为结果集合.

		// 3,处理limit.
		// 如果存在分页设置.
		List<Map<String, Object>> thirdStepResult = handlePaging(condition, secondStepResult);
		return thirdStepResult;
	}

	private List<Map<String, Object>> handlePaging(Condition condition, List<Map<String, Object>> recordList) {
		List<Map<String, Object>> result = null;
		if (condition.isPagingFlag()) {
			int len = recordList.size();
			int startIndex = condition.getPagingSettings()[0];
			int offSet = condition.getPagingSettings()[1];
			// 若所选结果集位置包含在 结果集合列表当中.
			if (startIndex < len) {
				int endIndex = startIndex;
				// 为了检索从某一个偏移量到记录集的结束所有的记录行，可以指定第二个参数为 -1：
				if (offSet == -1) {
					endIndex = len;
				} else {
					endIndex = startIndex + offSet;
				}
				if (endIndex > len) {
					endIndex = len;
				}
				result = recordList.subList(condition.getPagingSettings()[0], endIndex);
			} else {
				// 建立一个空列表作为返回结果.
				result = new ArrayList<Map<String, Object>>();
			}
		} else {
			result = recordList;
		}
		return result;
	}

	private List<Map<String, Object>> handleOrderBy(Condition condition, Object[] groupByAndaggregateArr) {
		// 排序.排序的列必须出现在数据集合当中.
		if (!condition.getOrderBySettings().isEmpty()) {
			// 存在group by,因此经过合并后的结果集合也可能只有一条记录.
			if (groupByAndaggregateArr.length > 1) {
				final Map<String, OrderByType> orderBySettings = condition.getOrderBySettings();
				final List<String> orderByFields = condition.getOrderByFields();
				// 遍历排序列.
				
				// 和group by类似,第一次排序与其它次分开.
				final String fristOrderByField = orderByFields.get(0);
				final String firstDataTablekey = getFieldKeyOfDataMap(condition, fristOrderByField);
				// 定义Comparator
				Comparator<Object> fristComparator = buildRecordComparator(orderBySettings, fristOrderByField,
								firstDataTablekey);
				// 执行首次排序.
				Arrays.sort(groupByAndaggregateArr, fristComparator);

				// 获取按上一次排序字段存在重复的记录,执行局部排序
				int len = orderByFields.size();
				if (len > 1) {
					// 方便局部排序,以数组格式进行二次处理.
					// 得到以firstDataTablekey
					// 无法区分的多条记录的索引,和分组很类似,只是经过排序后,两条key(比如id)的值一致的记录会紧挨在一起.

					// 构造第一级排序索引表.
					List<SqlKeyIndex> duplicateSqlKeyRecordIndexs = new ArrayList<SqlKeyIndex>();
					Map<Object, List<Integer>> fristColumnValAndIndexs = getUnDuplicateColumnVals(
									groupByAndaggregateArr, firstDataTablekey);
					for (Entry<Object, List<Integer>> entry : fristColumnValAndIndexs.entrySet()) {
						//前一次分组后子集记录数超过1的才需要二次排序.
						if (entry.getValue().size() > 1) {
							SqlKeyIndex sqlKeyIndex = new SqlKeyIndex();
							sqlKeyIndex.setVal(entry.getKey());
							sqlKeyIndex.setIndexs(entry.getValue());
							duplicateSqlKeyRecordIndexs.add(sqlKeyIndex);
						}
					}

					// 整体字段索引表.
					Map<String, List<SqlKeyIndex>> orderByIndexTable = new HashMap<String, List<SqlKeyIndex>>();
					orderByIndexTable.put(fristOrderByField, duplicateSqlKeyRecordIndexs);

					// 如果一级重复键索引表为空,那么不需要进行次级排序
					if (!duplicateSqlKeyRecordIndexs.isEmpty()) {
						for (int i = 1; i < len; ++i) {
							final String orderByField = orderByFields.get(i);
							final String dataTablekey = getFieldKeyOfDataMap(condition, orderByField);
							Comparator<Object> comparator = buildRecordComparator(orderBySettings, orderByField,
											dataTablekey);
							// 依据前一次的索引结果,执行局部排序,priorIndex里存储的都是之前的排序列无法完全区分两条记录的索引.
							List<SqlKeyIndex> priorSqlKeyRecordIndexs = orderByIndexTable.get(orderByFields.get(i - 1));
							for (SqlKeyIndex index : priorSqlKeyRecordIndexs) {
								int start = index.getIndexs().get(0);
								int end = index.getIndexs().get(index.getIndexs().size() - 1) + 1;
								Arrays.sort(groupByAndaggregateArr, start, end, comparator);
							}

							List<SqlKeyIndex> nowSqlKeyIndexs = new ArrayList<SqlKeyIndex>();
							// 再次构造下级的排序索引表.
							for (SqlKeyIndex priorIndex : priorSqlKeyRecordIndexs) {
								Object[] subArr = getSubArr(groupByAndaggregateArr, priorIndex.getIndexs());

								// 获取不重复一列值及其对应的列表记录索引.
								Map<Object, List<Integer>> nextColumnValAndIndexs = getUnDuplicateColumnVals(subArr,
												dataTablekey);
								for (Entry<Object, List<Integer>> entry : nextColumnValAndIndexs.entrySet()) {
									if (entry.getValue().size() > 1) {
										SqlKeyIndex sqlKeyIndex = new SqlKeyIndex();
										sqlKeyIndex.setVal(entry.getKey());
										sqlKeyIndex.setIndexs(getSubIndex(priorIndex.getIndexs(),
														nextColumnValAndIndexs.get(entry.getKey())));
										nowSqlKeyIndexs.add(sqlKeyIndex);
									}
								}

							}
							// 如果某一级别的排序索引已经不存在重复后,直接退出索引构建过程.
							if (nowSqlKeyIndexs.isEmpty()) {
								break;
							} else {
								orderByIndexTable.put(orderByField, nowSqlKeyIndexs);
							}
						}

					}
				}
				
			}
		}
		//将数组转换成列表.
		List<Map<String, Object>> orderByList = new ArrayList<Map<String, Object>>(groupByAndaggregateArr.length);
		for (Object obj : groupByAndaggregateArr) {
			Map<String, Object> record = (Map<String, Object>) obj;
			orderByList.add(record);
		}
		return orderByList;
	}

	private Object[] handleGroupByAndAggregateFunction(Condition condition, List<Map<String, Object>> distinctList) {
		Object[] groupByAndAggreateList = null;
		if (!condition.getGroupBySettings().isEmpty()) {
			Object[] groupByRecordArr = distinctList.toArray();

			// 构造分组索引表,取最终分组索引表.
			List<SqlKeyIndex> indexTable = buildGroupByIndexTable(condition, groupByRecordArr);
			int lastGroupSize = indexTable.size();
			Object[] groupByResult = new Object[lastGroupSize];
			// 只要存在聚合函数,那么分组后就需要继续处理聚合问题
			// 找到分组后的子数组,扫描condition,计算max,min,sum,count
			for (int i = 0; i < lastGroupSize; ++i) {
				SqlKeyIndex sqlKeyIndex = indexTable.get(i);
				if (sqlKeyIndex.getIndexs().size() == 1) {
					// 最终分组的索引只包含一条记录,不论是否存在聚合结构,都没必要进行计算.
					groupByResult[i] = groupByRecordArr[sqlKeyIndex.getIndexs().get(0)];
				} else {
					// 最终分组的索引包含多条记录
					Object[] subArr = getSubArr(groupByRecordArr, sqlKeyIndex.getIndexs());
					// 如果存在聚合函数,则需要在分组内部计算,也只会产生一条记录.
					if (!condition.isNoAggregateFunction()) {
						groupByResult[i] = handleWithAggregate(subArr, condition)[0];
					} else {
						// 如果无聚合函数,直接选择第一条记录.
						groupByResult[i] = subArr[0];
					}
				}
			}
			groupByAndAggreateList = groupByResult;
			// 如果存在排序执行整体排序.
		} else {
			// 如果没有group by字段
			// 如果出现聚合函数时,将只会产生一条记录,再次计算所有有聚合的字段值即可.
			// 同时不需要排序.
			if (!condition.isNoAggregateFunction()) {
				groupByAndAggreateList = handleWithAggregate(distinctList.toArray(), condition);
			} else {
				// 如果没有聚合函数,那么记录就不再需要 进行计算.
				groupByAndAggreateList = distinctList.toArray();
			}
		}
		return groupByAndAggreateList;
	}

	/**
	 * 构造比较器.
	 * 
	 * @param orderBySettings
	 * @param orderByField
	 * @param dataTablekey
	 * @return
	 */
	private Comparator<Object> buildRecordComparator(final Map<String, OrderByType> orderBySettings,
					final String orderByField, final String dataTablekey) {
		Comparator<Object> comparator = new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				Object obj1 = ((Map<String, Object>) o1).get(dataTablekey);
				Object obj2 = ((Map<String, Object>) o2).get(dataTablekey);
				if (orderBySettings.get(orderByField).equals(OrderByType.ASC)) {
					return ((Comparable<Object>) obj1).compareTo(obj2);
				} else {
					return ((Comparable<Object>) obj2).compareTo(obj1);
				}
			}
		};
		return comparator;
	}

	private Object[] handleWithAggregate(Object[] recordArr, Condition condition) {
		// 如果源记录只有一条,那么不必做任何二次聚合操作.
		if (recordArr.length == 1) {
			return recordArr;
		}
		// 只会存在一行记录.
		Object[] singleRecordResult = new Object[1];
		// sum只针对数字.
		// count 数量统计
		// min,max 对数字和字符均可,但以字符串比较为准.
		// 取第一条记录初始化目标数据.
		Map<String, Object> firstRecord = new HashMap<String, Object>((Map<String, Object>) recordArr[0]);

		if (!condition.getMaxSettings().isEmpty()) {
			// 循环max列表.
			for (String key : condition.getMaxSettings()) {
				String maxWrapperKey = Commons.maxWrapper(key);
				// 默认排序方式是从小到大.
				Object[] effectColumnVals = getColumnVals(recordArr, maxWrapperKey);
				// 排序.
				Arrays.sort(effectColumnVals);
				// 设置最大值
				firstRecord.put(maxWrapperKey, effectColumnVals[effectColumnVals.length - 1]);
			}
		}

		if (!condition.getMinSettings().isEmpty()) {
			for (String key : condition.getMinSettings()) {
				// 首先,检查当前列值是否已经在临时列存储当中.
				String minWrapperKey = Commons.minWrapper(key);

				Object[] effectColumnVals = getColumnVals(recordArr, minWrapperKey);
				// 排序.
				Arrays.sort(effectColumnVals);
				// 设置最小值
				firstRecord.put(minWrapperKey, effectColumnVals[0]);
			}
		}

		// 执行count统计计算.
		if (!condition.getCountSettings().isEmpty()) {
			for (String key : condition.getCountSettings()) {
				String countWrapperKey = Commons.countWrapper(key);
				caculateAccu(firstRecord, recordArr, countWrapperKey);
			}
		}

		// 执行求和计算.
		if (!condition.getSumSettings().isEmpty()) {
			// 求和时,只有数字才可以.其它返回0
			for (String key : condition.getSumSettings()) {
				String sumWrapperKey = Commons.sumWrapper(key);
				caculateAccu(firstRecord, recordArr, sumWrapperKey);
			}
		}
		singleRecordResult[0] = firstRecord;
		return singleRecordResult;
	}
	
	/**
	 * 目标列累加计算.
	 */
	
	/**
	 * 累加计算.
	 * @param accuContainer 存储计算结果.
	 * @param recordArr 数据集记录
	 * @param columnName 列名称
	 */
	private void caculateAccu(Map<String, Object> accuContainer, Object[] recordArr, String columnName){
		//获取列数据.
		Object[] effectColumnVals = getColumnVals(recordArr, columnName);

		//取第一个值.
		Object firstVal = effectColumnVals[0];
		if (firstVal instanceof Number) {
			if (firstVal instanceof Integer) {
				int sum = 0;
				for (Object val : effectColumnVals) {
					sum += ((Integer) val).intValue();
				}
				accuContainer.put(columnName, sum);
			} else if (firstVal instanceof Long) {
				long sum = 0;
				for (Object val : effectColumnVals) {
					sum += ((Long) val).longValue();
				}
				accuContainer.put(columnName, sum);
			} else if(firstVal instanceof BigDecimal){
				BigDecimal sum = new BigDecimal(0);
				for (Object val : effectColumnVals) {
					sum = sum.add((BigDecimal)val);
				}
				accuContainer.put(columnName, sum);
			}else {
				// float 和double采用 BigDemical
				BigDecimal sum = new BigDecimal(0);
				for (Object val : effectColumnVals) {
					sum = sum.add(new BigDecimal(val.toString()));
				}
				if (firstVal instanceof Double) {
					accuContainer.put(columnName, sum.doubleValue());
				} else if (firstVal instanceof Float) {
					accuContainer.put(columnName, sum.floatValue());
				} else {
					accuContainer.put(columnName, 0);
				}
			}
		} else {
			// 对其它数据类型,直接赋值0
			accuContainer.put(columnName, 0);
		}
	}

	/**
	 * 获取一列值(均转换为字符格式).
	 * 
	 * @param recordList
	 * @param key
	 * @return
	 */
	private Object[] getColumnVals(Object[] recordList, String key) {
		int len = recordList.length;
		Object[] result = new Object[len];
		for (int i = 0; i < len; ++i) {
			result[i] = ((Map<String, Object>) recordList[i]).get(key);
		}
		return result;
	}

	/**
	 * 构造各分组列索引表.
	 * 
	 * @param condition
	 * @param recordList
	 * @return
	 */
	private List<SqlKeyIndex> buildGroupByIndexTable(Condition condition, Object[] recordList) {
		// 初始化分组字段索引表.
		Map<String, List<SqlKeyIndex>> indexTable = new HashMap<String, List<SqlKeyIndex>>();
		// 对结果集合执行二次分组,第一次在数据库已执行分组
		// 通过分组得到分组信息,是否考虑下标表?

		// group by 第一级分开处理.
		List<String> groupByFields = condition.getGroupBySettings();
		// 获取数据记录结构中真实的键名称.
		String key = getFieldKeyOfDataMap(condition, groupByFields.get(0));
		// 获取不重复一列值及其对应的列表记录索引.
		Map<Object, List<Integer>> columnValAndIndexs = getUnDuplicateColumnVals(recordList, key);

		// 建立一级分组索引.
		List<SqlKeyIndex> sqlKeyIndexs = new ArrayList<SqlKeyIndex>();
		for (Object val : columnValAndIndexs.keySet()) {
			SqlKeyIndex sqlKeyIndex = new SqlKeyIndex();
			sqlKeyIndex.setVal(val);
			sqlKeyIndex.setIndexs(columnValAndIndexs.get(val));
			sqlKeyIndexs.add(sqlKeyIndex);
		}

		indexTable.put(groupByFields.get(0), sqlKeyIndexs);
		// 处理其它级别分组字段.
		int groupByFieldsSize = groupByFields.size();
		if (groupByFieldsSize > 1) {
			for (int i = 1; i < groupByFieldsSize; ++i) {
				// 首先获取上一级的分组索引,在上一级的分组索引上细化.
				List<SqlKeyIndex> priorSqlKeyIndexs = indexTable.get(groupByFields.get(i - 1));

				// 获取数据记录结构中真实的键名称.
				String nowKey = getFieldKeyOfDataMap(condition, groupByFields.get(i));
				// 建立分组索引.
				List<SqlKeyIndex> nowSqlKeyIndexs = new ArrayList<SqlKeyIndex>();
				for (SqlKeyIndex priorIndex : priorSqlKeyIndexs) {
					// 如果前一次分组后,某关键字值对应的数据记录是1,那么就不需要继续分组了.
					if (priorIndex.getIndexs().size() == 1) {
						// 索引位置一致.
						SqlKeyIndex sqlKeyIndex = new SqlKeyIndex();
						sqlKeyIndex.setIndexs(new ArrayList<Integer>(priorIndex.getIndexs()));
						// 从源列表的记录当中获取分组列对应的值.
						Map<String, Object> record = (Map<String, Object>) recordList[priorIndex.getIndexs().get(0)];
						String val = record.get(nowKey).toString();
						sqlKeyIndex.setVal(val);
						nowSqlKeyIndexs.add(sqlKeyIndex);
					} else {
						// 否则,继续细分.
						// a,获取需要细分的记录.
						Object[] subArr = getSubArr(recordList, priorIndex.getIndexs());
						// 获取不重复一列值及其对应的列表记录索引.
						Map<Object, List<Integer>> nextColumnValAndIndexs = getUnDuplicateColumnVals(subArr, nowKey);
						for (Object val : nextColumnValAndIndexs.keySet()) {
							SqlKeyIndex sqlKeyIndex = new SqlKeyIndex();
							sqlKeyIndex.setVal(val);
							sqlKeyIndex.setIndexs(getSubIndex(priorIndex.getIndexs(), nextColumnValAndIndexs.get(val)));
							nowSqlKeyIndexs.add(sqlKeyIndex);
						}
					}
				}
				// 将分组索引添加到索引总表.
				indexTable.put(groupByFields.get(i), nowSqlKeyIndexs);
			}
		}
		// 取最后一个分组索引.
		return indexTable.get(groupByFields.get(groupByFieldsSize - 1));
	}

	/**
	 * 获取下级分组的源列表索引. 保证每一级别分组的索引都关联到原始的数据记录.
	 * 
	 * @param priorIndexs
	 * @param newLocations
	 * @return
	 */
	private List<Integer> getSubIndex(List<Integer> priorIndexs, List<Integer> newLocations) {
		List<Integer> result = new ArrayList<Integer>(newLocations.size());
		for (Integer location : newLocations) {
			result.add(priorIndexs.get(location));
		}
		return result;
	}

	/**
	 * 获取子数组.
	 * 
	 * @param arr
	 * @param selectedIndexs
	 * @return
	 */
	private Object[] getSubArr(Object[] arr, List<Integer> selectedIndexs) {
		Object[] newArr = new Object[selectedIndexs.size()];
		int i = 0;
		for (Integer index : selectedIndexs) {
			newArr[i++] = arr[index];
		}
		return newArr;
	}

	/**
	 * 获取非重复一列值. 并标出索引记录的下标 比如id=1的记录 会有3条,在列表中的下标可能是 0,2,6
	 * 
	 * @param recordArr
	 * @param key
	 *            数据记录列名称
	 * @return
	 */
	private Map<Object, List<Integer>> getUnDuplicateColumnVals(Object[] recordArr, String key) {
		Map<Object, List<Integer>> result = new HashMap<Object, List<Integer>>();
		int len = recordArr.length;
		for (int i = 0; i < len; ++i) {
			Map<String, Object> map = (Map<String, Object>) recordArr[i];
			Object val = map.get(key);
			if (result.containsKey(val)) {
				result.get(val).add(i);
			} else {
				List<Integer> indexs = new ArrayList<Integer>();
				indexs.add(i);
				result.put(val, indexs);
			}
		}
		return result;
	}

	/**
	 * 查找数据集当中groupByField的名称. 如果字段没有被聚合函数包装,直接是groupByField 例如:select id,name
	 * from users; 结果列表单条记录集合 map 使用map.get("id")取值.
	 * 如果字段被聚合函数包装,以max为例,名称为max(groupByField) 例如:select id,max(name) from users
	 * group by id; 结果列表单条记录集合 map 使用map.get("max(name)")取值.
	 * 
	 * @return
	 */
	private String getFieldKeyOfDataMap(Condition condition, String groupByField) {
		String result = null;
		if (condition.getMaxSettings().contains(groupByField)) {
			result = AggregationType.MAX.name() + SeperatorSymbol.LEFT_PARENTHESIS + groupByField
							+ SeperatorSymbol.RIGHT_PARENTHESIS;
		} else if (condition.getMinSettings().contains(groupByField)) {
			result = AggregationType.MIN.name() + SeperatorSymbol.LEFT_PARENTHESIS + groupByField
							+ SeperatorSymbol.RIGHT_PARENTHESIS;
		} else if (condition.getMinSettings().contains(groupByField)) {
			result = AggregationType.COUNT.name() + SeperatorSymbol.LEFT_PARENTHESIS + groupByField
							+ SeperatorSymbol.RIGHT_PARENTHESIS;
		} else if (condition.getMinSettings().contains(groupByField)) {
			result = AggregationType.SUM.name() + SeperatorSymbol.LEFT_PARENTHESIS + groupByField
							+ SeperatorSymbol.RIGHT_PARENTHESIS;
		} else {
			result = groupByField;
		}
		return result;
	}

}
