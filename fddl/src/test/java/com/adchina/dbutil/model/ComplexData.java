package com.adchina.dbutil.model;

import java.util.Date;

public class ComplexData{
	private int id;
	private long age;
	
	private Date date;
	
	public ComplexData(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ComplexData [id=" + id + ", age=" + age + ", date=" + date + "]";
	}
	
}
