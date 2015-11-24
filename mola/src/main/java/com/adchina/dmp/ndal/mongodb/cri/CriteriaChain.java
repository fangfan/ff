package com.adchina.dmp.ndal.mongodb.cri;

import com.adchina.dmp.ndal.mongodb.Constants;
import com.adchina.dmp.ndal.mongodb.op.LogicOperator;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by F.Fang on 2015/3/2.
 * Version :2015/3/2
 */
public class CriteriaChain {

    private List<CriteriaGroup> groupList = new ArrayList<>();

    private LogicOperator groupOp;

    public CriteriaChain append(CriteriaGroup group) {
        this.groupList.add(group);
        return this;
    }

    public CriteriaChain OR() {
        this.groupOp = LogicOperator.OR;
        return this;
    }

    public CriteriaChain AND() {
        this.groupOp = LogicOperator.AND;
        return this;
    }

    public CriteriaChain PARAM(Object obj) {
        // obj --> map, 默认将多层的对象转换为.

        return this;
    }

    public CriteriaChain PARAM(Map map) {
        for (CriteriaGroup group : groupList) {
            for (Criteria criteria : group.getGroup()) {
                // 如果没有被设置值.
                if(!criteria.isValueSet()){
                    fillCriteriaValue(criteria, map);
                }
            }
        }
        return this;
    }

    private void fillCriteriaValue(Criteria criteria, Map params) {
        String key = criteria.getKey();
        if (params.containsKey(key)) {
            criteria.setValue(params.get(key));
        } else {
            if (key.contains(Constants.POINT)) {
                String[] subKeys = key.split(Constants.POINT);
                String subKey = null;
                Map subMap = params;
                for (int i = 0, len = subKeys.length; i < len; ++i) {
                    subKey = subKeys[i];
                    if (i != len - 1) {
                        Object obj = subMap.get(subKey);
                        if (obj instanceof Map) {
                            subMap = (Map) obj;
                        } else {
                            // map结构无法符合赋值要求.
                            break;
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("params map(" + params + ")not contains key(+" + key + ")!");
            }
        }

    }


    public DBObject build() {
        DBObject obj = null;
        if (groupList.isEmpty()) {
            throw new IllegalArgumentException("criteria chain is blank!");
        }
        if (groupList.size() == 1) {
            return groupList.get(0).build();
        } else {
            obj = new BasicDBObject();
            BasicDBList list = new BasicDBList();
            for (CriteriaGroup group : groupList) {
                list.add(group.build());
            }
            obj.put(groupOp.getValue(), list);
        }
        return obj;
    }
}
