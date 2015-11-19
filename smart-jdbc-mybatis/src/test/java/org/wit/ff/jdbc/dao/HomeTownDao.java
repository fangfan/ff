package org.wit.ff.jdbc.dao;

import org.wit.ff.jdbc.id.BatchInsertEntities;
import org.wit.ff.jdbc.model.HomeTown;
import org.wit.ff.jdbc.query.Criteria;
import org.wit.ff.jdbc.result.CriteriaResult;

import java.util.List;

/**
 * Created by F.Fang on 2015/11/17.
 * Version :2015/11/17
 */
public interface HomeTownDao {

    int insertList(List<HomeTown> entities);

    List<HomeTown> findAll();

    List<HomeTown> find(int id,Criteria criteria);

    void batchInsert(BatchInsertEntities<HomeTown> batchEntities);

}
