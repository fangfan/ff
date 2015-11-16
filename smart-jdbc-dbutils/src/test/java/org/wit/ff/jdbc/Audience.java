package org.wit.ff.jdbc;

/**
 * Created by F.Fang on 2015/2/16.
 * Version :2015/2/16
 */
public class Audience {
    private Integer id;

    private String name;

    private Double pay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPay() {
        return pay;
    }

    public void setPay(Double pay) {
        this.pay = pay;
    }

    @Override
    public String toString() {
        return "Audience{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pay=" + pay +
                '}';
    }
}
