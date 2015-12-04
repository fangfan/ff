package org.wit.fddl.sql;

import java.util.List;

/**
 * 分组字段或排序字段索引结构.
 * @author F.Fang
 *
 */
public class SqlKeyIndex {
	
	/**
	 * sql字段的值.
	 */
	private Object val;
	
	/**
	 * 数据列表中一条记录的某个属性的值为key的记录在数据列表中的下标索引集合.
	 * List<Map<String,Object>> list 存在两条记录
	 * 第一条记录 {id=1,name='ff'} 下标为0
	 * 第二条记录 {id=2,name='gg'} 下标为1
	 * 因此,假如以id属性作为索引时,key=1的记录选择时 indexs = {0};
	 */
	private List<Integer> indexs;

	public List<Integer> getIndexs() {
		return indexs;
	}

	public void setIndexs(List<Integer> indexs) {
		this.indexs = indexs;
	}

	public Object getVal() {
		return val;
	}

	public void setVal(Object val) {
		this.val = val;
	}

}
