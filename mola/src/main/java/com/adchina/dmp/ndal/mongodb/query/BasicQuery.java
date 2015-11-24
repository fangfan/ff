package com.adchina.dmp.ndal.mongodb.query;

import com.adchina.dmp.ndal.IQuery;
import com.adchina.dmp.ndal.mongodb.Constants;
import com.adchina.dmp.ndal.mongodb.cri.CriteriaChain;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by F.Fang on 2015/2/27.
 * Version :2015/2/27
 */
public class BasicQuery implements IQuery{

    /**
     * 文档聚合名.
     */
    private String collection;

    /**
     * 返回类型.
     */
    private Class<?> resultType;

    /**
     * where条件.
     */
    protected DBObject where;

    /**
     * 排序字段.
     */
    protected DBObject sort;

    /**
     * 选择字段.
     */
    protected DBObject select;

    /**
     * 分页skip.
     */
    protected int skip;

    /**
     * 分页limit.
     */
    protected int limit;

    /**
     * 分页.
     */
    protected boolean page;

    /**
     * 忽略MongoDB自动生成的id.
     */
    protected boolean ignoreId;

    public BasicQuery(String collection,Class<?> resultType){
        this.resultType = resultType;
        this.collection = collection;
        this.ignoreId = true;
    }

    public BasicQuery OPENID(){
        this.ignoreId = false;
        return this;
    }

    public BasicQuery WHERE(CriteriaChain cc){
        if(cc!=null){
            where = cc.build();
        }
        return this;
    }

    /**
     * 指定选择字段和过滤字段.
     * 可以只选其一,也可以都选择.
     * 多个字段用逗号","分隔.
     *
     * @param includeFields   选择字段.
     * @param excludeFields 过滤字段.
     * @return this。
     */
    public BasicQuery SELECT(String includeFields, String excludeFields) {
        if (StringUtils.isNotBlank(includeFields)) {
            if(select==null){
                select = new BasicDBObject();
            }

            String[] fields = StringUtils.split(includeFields.trim(), Constants.COMMA);
            for (String field : fields) {
                select.put(field, 1);
            }
        }
        if (StringUtils.isNotBlank(excludeFields)) {
            if (select == null) {
                select = new BasicDBObject();
            }
            String[] fields = StringUtils.split(excludeFields.trim(), Constants.COMMA);
            for (String field : fields) {
                select.put(field, 0);
            }
        }
        return this;
    }

    /**
     * 分页字段.
     *
     * @param skip
     * @param limit
     * @return
     */
    public BasicQuery PAGE(int skip, int limit) {
        if (skip >= 0 && limit > 0) {
            this.skip = skip;
            this.limit = limit;
            this.page = true;
        }
        return this;
    }

    /**
     * 增序排序.
     *
     * @param sortFields 排序字段.
     * @return this.
     */
    public BasicQuery SORT_ASC(String sortFields) {
        if (sort == null) {
            sort = new BasicDBObject();
        }
        if (StringUtils.isNotBlank(sortFields)) {
            String[] fields = StringUtils.split(sortFields.trim(), Constants.COMMA);
            for (String field : fields) {
                sort.put(field, 1);
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
    public BasicQuery SORT_DESC(String sortFields) {
        if (sort == null) {
            sort = new BasicDBObject();
        }
        if (StringUtils.isNotBlank(sortFields)) {
            String[] fields = StringUtils.split(sortFields.trim(), Constants.COMMA);
            for (String field : fields) {
                sort.put(field, -1);
            }
        }
        return this;
    }

    public BasicQuery build(){
        // 目前只需要对_id做适应.
        if(ignoreId){
            if(select==null){
                select = new BasicDBObject("_id",0);
            }else{
                select.put("_id",0);
            }
        }
        return this;
    }

    public String getCollection() {
        return collection;
    }

    public boolean isPage() {
        return page;
    }

    public DBObject getWhere() {
        return where;
    }

    public DBObject getSort() {
        return sort;
    }

    public DBObject getSelect() {
        return select;
    }

    public int getSkip() {
        return skip;
    }

    public int getLimit() {
        return limit;
    }

    public Class<?> getResultType() {
        return resultType;
    }
}
