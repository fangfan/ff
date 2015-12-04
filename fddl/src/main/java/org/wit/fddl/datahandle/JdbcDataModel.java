package org.wit.fddl.datahandle;

import java.util.List;
import java.util.Map;

/**
 * 数据模型.
 * @author F.Fang
 *
 */
public class JdbcDataModel extends DataModel{
	
	/**
	 * 数据集合.
	 * 为空表示数据记录数为0
	 */
	protected List<Map<String, Object>> data;
	
	/**
	 * 处理数据失败的模型.
	 * @param timeExpend
	 * @param errorFlag
	 * @param msg
	 */
	public JdbcDataModel(long timeExpend, String msg) {
		super();
		this.timeExpend = timeExpend;
		this.errorFlag = true;
		this.msg = msg;
	}

	/**
	 * 处理数据成功并存在数据.
	 * @param data
	 * @param count
	 * @param timeExpend
	 */
	public JdbcDataModel(List<Map<String, Object>> data, int count, long timeExpend) {
		super();
		this.data = data;
		this.timeExpend = timeExpend;
	}

	/**
	 * 处理数据成功但结果集为空.
	 * @param timeExpend
	 */
	public JdbcDataModel(long timeExpend) {
		super();
		this.timeExpend = timeExpend;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

}
