package org.wit.ff.jdbc.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.wit.ff.jdbc.id.IdGenerator;

/**
 * Created by F.Fang on 2015/11/17.
 * Version :2015/11/17
 */
public class HomeTown implements IdGenerator {
    private int id;

    private String name;

    private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public void parseGenKey(Object[] value) {
        if(value!=null && value.length == 1){
            this.id = Integer.valueOf(value[0].toString());
        }
    }
}
