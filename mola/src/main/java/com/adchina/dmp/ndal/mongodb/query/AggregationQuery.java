package com.adchina.dmp.ndal.mongodb.query;

import com.adchina.dmp.ndal.mongodb.Constants;
import com.adchina.dmp.ndal.mongodb.cri.CriteriaChain;
import com.adchina.dmp.ndal.mongodb.op.AggregateOperator;
import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by F.Fang on 2015/3/3.
 * 聚合query.
 * Version :2015/3/3
 */
public class AggregationQuery extends BasicQuery {

    /**
     * groupBy字段.
     */
    protected DBObject groupBy;

    /**
     * 最终的group信息.
     */
    protected DBObject group;

    /**
     * 聚合通道.
     */
    protected List<DBObject> pipeline;

    /**
     * 聚合参数.
     */
    protected AggregationOptions aggregationOptions;

    public AggregationQuery(String collection, Class<?> resultType) {
        super(collection, resultType);
        this.ignoreId = false;
        this.group = new BasicDBObject();
    }

    public AggregationQuery options(AggregationOptions aggregationOptions) {
        this.aggregationOptions = aggregationOptions;
        return this;
    }

    /**
     * 分组字段,多个用','隔开.
     *
     * @param fields
     * @return
     */
    public AggregationQuery GROUP(String fields) {
        if (StringUtils.isBlank(fields)) {
            throw new IllegalArgumentException("group fields can't be blank!");
        }

        if (groupBy == null) {
            groupBy = new BasicDBObject();
        }

        String[] fieldArr = fields.split(Constants.COMMA);
        for (String field : fieldArr) {
            groupBy.put(field, Constants.VALUE_PREFIX + field);
        }
        group.put(Constants._ID, groupBy);
        return this;
    }

    public AggregationQuery AG(Aggregation aggregation) {
        if (aggregation == null || aggregation.getOp() == null || StringUtils.isBlank(aggregation.getKey())) {
            throw new IllegalArgumentException("aggregation(" + aggregation + ") is illegal!");
        }
        fillGroup(aggregation);
        return this;
    }

    public AggregationQuery AG(String[] agFields, AggregateOperator op) {
        if (agFields == null || agFields.length == 0 || op == null) {
            throw new IllegalArgumentException("aggregation params(agFields=" + agFields + ",op=" + op + ") is illegal!");
        }
        if (op == AggregateOperator.COUNT) {
            throw new IllegalArgumentException("operator 'count' is not allowed use this builder!");
        }
        for (String field : agFields) {
            fillGroup(new Aggregation(field, op));
        }
        return this;
    }

    private void fillGroup(Aggregation aggregation) {
        if (aggregation.getOp() == AggregateOperator.COUNT) {
            this.group.put(aggregation.getKey(), new BasicDBObject(aggregation.getOp().getValue(), 1));
        } else {
            this.group.put(aggregation.getKey(),
                    new BasicDBObject(aggregation.getOp().getValue(), Constants.VALUE_PREFIX + aggregation.getKey()));
        }
    }

    /**
     * .
     *
     * @param cc
     * @return
     */
    public AggregationQuery WHERE(CriteriaChain cc) {
        super.WHERE(cc);
        return this;
    }

    /**
     * @param includeFields 选择字段.
     * @param excludeFields 过滤字段.
     * @return
     */
    public AggregationQuery SELECT(String includeFields, String excludeFields) {
        super.SELECT(includeFields, excludeFields);
        return this;
    }

    /**
     * 增序排序.
     *
     * @param sortFields 排序字段.
     * @return this.
     */
    public AggregationQuery SORT_ASC(String sortFields) {
        if (sort == null) {
            sort = new BasicDBObject();
        }
        if (StringUtils.isNotBlank(sortFields)) {
            String prefix = Constants._ID + Constants.POINT;
            String[] fields = StringUtils.split(sortFields.trim(), Constants.COMMA);
            for (String field : fields) {
                sort.put(prefix + field, 1);
            }
        }
        return this;
    }

    /**
     * 降序排序.
     *
     * @param sortFields 降序字段.
     * @return this.
     */
    public AggregationQuery SORT_DESC(String sortFields) {
        if (sort == null) {
            sort = new BasicDBObject();
        }
        if (StringUtils.isNotBlank(sortFields)) {
            String prefix = Constants._ID + Constants.POINT;
            String[] fields = StringUtils.split(sortFields.trim(), Constants.COMMA);
            for (String field : fields) {
                sort.put(prefix + field, -1);
            }
        }
        return this;
    }

    /**
     * 重写build方法.
     *
     * @return this.
     */
    public AggregationQuery build() {
        this.pipeline = new ArrayList<DBObject>();
        // match.
        if (this.where != null) {
            pipeline.add(new BasicDBObject("$match", this.where));
        }
        // projection.
        if (this.select != null) {
            pipeline.add(new BasicDBObject("$projection", this.select));
        }

        // group
        if (this.groupBy == null) {
            throw new IllegalArgumentException("group by fields must be set!");
        }
        pipeline.add(new BasicDBObject("$group", group));

        // sort
        if (this.sort != null) {
            pipeline.add(new BasicDBObject("$sort", this.sort));
        }

        // limit
        if (this.limit > 0) {
            pipeline.add(new BasicDBObject("$limit", this.limit));
        }

        // skip
        if (this.skip > 0) {
            pipeline.add(new BasicDBObject("$skip", this.skip));
        }
        return this;
    }

    public List<DBObject> getPipeline() {
        return pipeline;
    }

    public AggregationOptions getAggregationOptions() {
        return aggregationOptions;
    }
}
