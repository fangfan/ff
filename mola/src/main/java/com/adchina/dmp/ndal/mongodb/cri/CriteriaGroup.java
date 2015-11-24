package com.adchina.dmp.ndal.mongodb.cri;

import com.adchina.dmp.ndal.mongodb.op.LogicOperator;
import com.adchina.dmp.ndal.mongodb.op.MathOperator;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by F.Fang on 2015/3/2.
 * Version :2015/3/2
 */
public class CriteriaGroup {

    /**
     * 每个group的内部连接操作符.
     */
    private LogicOperator cop;

    private List<Criteria> group = new ArrayList<>();

    public CriteriaGroup append(Criteria criteria) {
        if (criteria != null) {
            group.add(criteria);
        }
        return this;
    }

    public CriteriaGroup append(String[] keys, MathOperator mop) {
        if (keys == null || mop == null || keys.length == 0) {
            throw new IllegalArgumentException("Criteria params error!");
        }
        if (keys != null) {
            for (int i = 0, len = keys.length; i < len; ++i) {
                if (StringUtils.isNotBlank(keys[i])) {
                    Criteria criteria = new Criteria(keys[i], mop);
                    group.add(criteria);
                }
            }
        }
        return this;
    }

    public CriteriaGroup append(String[] keys, MathOperator[] mops) {
        if (keys == null || mops == null || keys.length == 0 || keys.length != mops.length) {
            throw new IllegalArgumentException("Criteria params error!");
        }
        if (keys != null) {
            for (int i = 0, len = keys.length; i < len; ++i) {
                if (StringUtils.isNotBlank(keys[i])) {
                    Criteria criteria = new Criteria(keys[i], mops[i]);
                    group.add(criteria);
                }
            }
        }
        return this;
    }

    public CriteriaGroup append(String[] keys, MathOperator mop, Object[] params) {
        if (keys == null || mop == null || params == null || keys.length == 0 || keys.length != params.length) {
            throw new IllegalArgumentException("Criteria params error!");
        }
        if (keys != null) {
            for (int i = 0, len = keys.length; i < len; ++i) {
                if (StringUtils.isNotBlank(keys[i])) {
                    Criteria criteria = new Criteria(keys[i], mop, params[i]);
                    group.add(criteria);
                }
            }
        }
        return this;
    }

    public CriteriaGroup append(String[] keys, MathOperator[] mops, Object[] params) {
        if (keys == null || mops == null || params == null || keys.length == 0 || keys.length != params.length || keys.length != mops.length) {
            throw new IllegalArgumentException("Criteria params error!");
        }
        if (keys != null) {
            for (int i = 0, len = keys.length; i < len; ++i) {
                if (StringUtils.isNotBlank(keys[i])) {
                    Criteria criteria = new Criteria(keys[i], mops[i], params[i]);
                    group.add(criteria);
                }
            }
        }
        return this;
    }


    public CriteriaGroup OR() {
        this.cop = LogicOperator.OR;
        return this;
    }

    public CriteriaGroup AND() {
        this.cop = LogicOperator.AND;
        return this;
    }

    public DBObject build() {
        DBObject obj = null;
        if (group.isEmpty()) {
            throw new IllegalArgumentException("criteria group is blank!");
        }
        if (group.size() == 1) {
            obj = group.get(0).build();
        } else {
            obj = new BasicDBObject();
            BasicDBList list = new BasicDBList();
            for (Criteria criteria : group) {
                list.add(criteria.build());
            }
            obj.put(cop.getValue(), list);
        }
        return obj;
    }

    public List<Criteria> getGroup() {
        return group;
    }
}
