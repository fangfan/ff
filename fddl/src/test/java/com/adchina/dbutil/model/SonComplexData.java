package com.adchina.dbutil.model;

public class SonComplexData extends ComplexData{
	
	private long money;

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	@Override
	public String toString() {
		return "SonComplexData [money=" + money + "],"+super.toString();
	}
	
	

}
