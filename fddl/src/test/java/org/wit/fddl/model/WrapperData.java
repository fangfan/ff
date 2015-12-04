package org.wit.fddl.model;

public class WrapperData {
	
	private Integer id;
	
	private long age;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "WrapperData [id=" + id + ", age=" + age + "]";
	}

}
