package org.wit.ff.jdbc.dao;

import org.wit.ff.jdbc.id.BatchInsertEntities;
import org.wit.ff.jdbc.model.HomeTown;

import java.util.List;

/**
 * Created by F.Fang on 2015/11/17.
 * Version :2015/11/17
 */
public interface HomeTownDao {

    int insertList(List<HomeTown> entities);

    List<HomeTown> findAll();

    void batchInsert(BatchInsertEntities<HomeTown> batchEntities);

}
