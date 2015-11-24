package com.adchina.dmp.ndal.model;

import java.util.Date;

/**
 * Created by F.Fang on 2015/3/12.
 * Version :2015/3/12
 */
public class DateUser {

    private String name;

    private Integer age;

    private Integer gender;

    private Date date;

    private Date createdDate;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DateUser{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", date=" + date +
                ", createDate=" + createdDate +
                '}';
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
