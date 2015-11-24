package com.adchina.dmp.ndal.mongodb.op;

/**
 * Created by F.Fang on 2015/3/3.
 * Version :2015/3/3
 */
public enum AggregateOperator {

    SUM("$sum"), AVG("$avg"), MIN("$min"), MAX("$max"), FIRST("$first"),
    LAST("$last"), PUSH("$push"), ADDTOSET("$addToSet"), COUNT("$sum");

    private String value;

    AggregateOperator(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
