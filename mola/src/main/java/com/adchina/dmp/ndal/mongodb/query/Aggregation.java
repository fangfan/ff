package com.adchina.dmp.ndal.mongodb.query;

import com.adchina.dmp.ndal.mongodb.op.AggregateOperator;

/**
 * Created by F.Fang on 2015/3/3.
 * Version :2015/3/3
 */
public class Aggregation {

    private String key;

    private AggregateOperator op;

    public Aggregation(String key){
        this.key = key;
    }

    public Aggregation(String key, AggregateOperator op){
        this.key = key;
        this.op = op;
    }

    public Aggregation sum(){
        this.op = AggregateOperator.SUM;
        return this;
    }

    public Aggregation avg(){
        this.op = AggregateOperator.AVG;
        return this;
    }

    public Aggregation min(){
        this.op = AggregateOperator.MIN;
        return this;
    }

    public Aggregation max(){
        this.op = AggregateOperator.MAX;
        return this;
    }

    public Aggregation first(){
        this.op = AggregateOperator.FIRST;
        return this;
    }

    public Aggregation last(){
        this.op = AggregateOperator.LAST;
        return this;
    }

    public Aggregation push(){
        this.op = AggregateOperator.PUSH;
        return this;
    }

    public Aggregation addToSet(){
        this.op = AggregateOperator.ADDTOSET;
        return this;
    }

    public Aggregation count(){
        this.op = AggregateOperator.COUNT;
        return this;
    }

    public String getKey() {
        return key;
    }

    public AggregateOperator getOp() {
        return op;
    }

    @Override
    public String toString() {
        return "Aggregation{" +
                "key='" + key + '\'' +
                ", op=" + op +
                '}';
    }
}
