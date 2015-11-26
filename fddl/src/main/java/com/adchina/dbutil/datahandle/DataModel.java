package com.adchina.dbutil.datahandle;


public class DataModel {
	
	
	/**
	 * 数据记录数.
	 */
	protected int count;
	
	/**
	 * 时间花费.
	 */
	protected long timeExpend;
	
	/**
	 * 错误标识.
	 * ture标识数据处理发生错误.
	 */
	protected boolean errorFlag;
	
	/**
	 * 失败信息.
	 */
	protected String msg;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getTimeExpend() {
		return timeExpend;
	}

	public void setTimeExpend(long timeExpend) {
		this.timeExpend = timeExpend;
	}

	public boolean isErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getSqlExecuteInfo(){
		StringBuffer sb = new StringBuffer();
		if(errorFlag){
			sb.append("语句执行失败,"+",时间花费:"+timeExpend+" ms,"+"失败信息:"+msg);
		}else{
			sb.append("语句执行成功,记录数:"+count+",时间花费:"+timeExpend+" ms");
		}
		return sb.toString();
	}

}
