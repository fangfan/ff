package com.adchina.dmp.ndal.mongodb.op;

/**
 * Created by F.Fang on 2015/3/2.
 * 连接运算符号.
 * Version :2015/3/2
 */
public enum LogicOperator {
    AND("$and"), OR("$or");

    private String value;

    LogicOperator(String value){this.value = value;}

    public String getValue() {
        return value;
    }
}
