package com.adchina.dmp.ndal.model;

import com.adchina.dmp.ndal.mongodb.annotations.Schema;

/**
 * Created by F.Fang on 2015/3/4.
 * Version :2015/3/4
 */
public class SchemaUser {

    private String name;

    private Integer age;

    private Integer gender;

    @Schema
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SchemaUser{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", address=" + address +
                '}';
    }
}
