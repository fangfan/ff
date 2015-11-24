package com.adchina.dmp.ndal.model;

/**
 * Created by F.Fang on 2015/2/26.
 * Version :2015/2/26
 */
public class Address {

    private String country;

    private String city;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
