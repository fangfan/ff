package org.wit.ff.model;

import java.io.Serializable;

/**
 * Created by F.Fang on 2015/9/15.
 * Version :2015/9/15
 */
public class User implements Serializable{

    private long id;

    private String name;

    private String email;

    public User(){

    }

    public User(long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
