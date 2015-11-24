package com.adchina.dmp.ndal.mongodb.cri;

import com.adchina.dmp.ndal.mongodb.op.MathOperator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by F.Fang on 2015/3/1.
 * Version :2015/3/1
 */
public class Criteria {

    private String key;

    private MathOperator op;

    private Object value;

    private boolean isValueSet;

    public Criteria(String key){
        this.key = key;
    }

    public Criteria(String key, Object value){
        this.key = key;
        this.value = value;
        this.isValueSet = true;
    }

    public Criteria(String key, MathOperator op){
        this.key = key;
        this.op = op;
    }

    public Criteria(String key, MathOperator op, Object value){
        this.key = key;
        this.op = op;
        this.value = value;
        this.isValueSet = true;
    }

    public Criteria eq(){
        this.op = MathOperator.EQ;
        return this;
    }

    /**
     * !=
     * @return this.
     */
    public Criteria ne() {
        this.op = MathOperator.NE;
        return this;
    }

    /**
     * <
     * @return this.
     */
    public Criteria lt() {
        this.op = MathOperator.LT;
        return this;
    }

    /**
     * <=
     * @return this.
     */
    public Criteria lte() {
        this.op = MathOperator.LTE;
        return this;
    }

    /**
     * >
     * @return this.
     */
    public Criteria gt() {
        this.op = MathOperator.GT;
        return this;
    }

    /**
     * >=.
     * @return this.
     */
    public Criteria gte() {
        this.op = MathOperator.GTE;
        return this;
    }

    /**
     * in.
     * @return this.
     */
    public Criteria in() {
        this.op = MathOperator.IN;
        return this;
    }

    /**
     * not in.
     * @return this.
     */
    public Criteria nin() {
        this.op = MathOperator.NTN;
        return this;
    }

    public DBObject build(){
        DBObject obj = new BasicDBObject();
        if(this.op == MathOperator.EQ){
            obj.put(this.key, this.value);
        }else{
            obj.put(this.key, new BasicDBObject(this.op.getTag(), this.value));
        }
        return obj;
    }

    public String getKey() {
        return key;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public MathOperator getOp() {
        return op;
    }

    public boolean isValueSet() {
        return isValueSet;
    }
}
