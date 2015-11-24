package com.adchina.dmp.ndal;

import java.util.Collection;

/**
 * Created by F.Fang on 2015/2/26.
 * Version :2015/2/26
 */
public interface IDataAccessor {

    <T> Collection<T> query(IQuery q);

    void update(IUpdater u);

    void insert(IUpdater i);

    void delete(IUpdater d);

}
