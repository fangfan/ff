package org.wit.fddl.model;

import java.util.Date;



/**
 * 订单投放每日报告
 * 
 * @author David.Dai
 * 
 */
public class DeliveryDailyData{
	/**
	 * 
	 */
	private Date settledTime;
	private long dailyUV;
	private long todateUV;

	public DeliveryDailyData() {
		super();
	}

	public DeliveryDailyData(Date settledTime, long dailyUV, long todateUV) {
		this.settledTime = settledTime;
		this.dailyUV = dailyUV;
		this.todateUV = todateUV;
	}

	public Date getSettledTime() {
		return settledTime;
	}

	public void setSettledTime(Date settledTime) {
		this.settledTime = settledTime;
	}	
	
	public long getDailyUV() {
		return dailyUV;
	}

	public void setDailyUV(long dailyUV) {
		this.dailyUV = dailyUV;
	}

	public long getTodateUV() {
		return todateUV;
	}

	public void setTodateUV(long todateUV) {
		this.todateUV = todateUV;
	}

	@Override
	public String toString() {
		return "DeliveryDailyData [settledTime=" + settledTime + ", dailyUV=" + dailyUV + ", todateUV=" + todateUV
						+ "]";
	}

	
}
