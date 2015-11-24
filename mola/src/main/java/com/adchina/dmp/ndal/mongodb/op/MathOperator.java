package com.adchina.dmp.ndal.mongodb.op;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by F.Fang on 2015/3/2.
 * Version :2015/3/2
 */
public enum MathOperator {

    EQ("$eq", new String[]{"="}),
    NE("$ne", new String[]{"!="}),
    LT("$lt", new String[]{"<"}),
    LTE("$lte", new String[]{"<="}),
    GT("$gt", new String[]{">"}),
    GTE("$gte", new String[]{">="}),
    IN("$in", new String[]{"in"}),
    NTN("$nin", new String[]{"not in"});

    private String tag;

    private String[] extArr;

    private static Map<String, MathOperator> common;

    private static Map<String, MathOperator> extMath;

    private MathOperator(String tag, String[] extArr) {
        this.tag = tag;
        this.extArr = extArr;
        getCommon().put(tag, this);
        for (String str : extArr) {
            getExtMath().put(str, this);
        }
    }

    public static MathOperator parse(String val) {
        if (StringUtils.isBlank(val)) {
            return null;
        } else {
            String key = val.trim();
            if (common.containsKey(key)) {
                return common.get(key);
            } else if (extMath.containsKey(key)) {
                return extMath.get(key);
            } else {
                return null;
            }
        }
    }

    public String getTag() {
        return tag;
    }

    public String[] getExtArr() {
        return extArr;
    }

    private static Map<String, MathOperator> getCommon() {
        if(common==null){
            common = new HashMap<>();
        }
        return common;
    }

    private static Map<String, MathOperator> getExtMath() {
        if(extMath==null){
            extMath = new HashMap<>();
        }
        return extMath;
    }
}
