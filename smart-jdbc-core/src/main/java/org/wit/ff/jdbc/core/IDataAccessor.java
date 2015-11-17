package org.wit.ff.jdbc.core;


import org.wit.ff.jdbc.converter.ParamsConverter;

import java.util.List;
import java.util.Map;

/**
 * Created by F.Fang on 2015/3/31.
 * 简单公共数据访问层.
 * Version :2015/3/31
 */
public interface IDataAccessor {

    /**
     * 查询
     * @param sql
     * @param resultType 对象类型
     * @param <T>
     * @return
     */
    <T> List<T> query(String sql, Class<T> resultType);

    /**
     * 查询,如果返回类型是具体类型,那么就采用
     * @param sql 查询语句
     * @param params 查询参数
     * @param resultType 返回类型
     * @param <T>
     * @return
     */
    <T> List<T> query(String sql, Object[] params, Class<T> resultType);


    /**
     * insert 对象列表.
     * @param sql insert语句.
     * @param params 对象参数列表.
     * @param paramsType 参数类型.
     * @param converter 参数转换器.
     * @param <T>
     */
    <T> void insert(String sql, List<T> params, Class<T> paramsType, ParamsConverter<T> converter);

    /**
     * insert batch.
     * @param sql
     * @param params
     */
    void insert(String sql, Object[][] params);

    /**
     * single insert.
     * @param sql
     * @param params
     */
    void insert(String sql, Object[] params);

    /**
     * batch update.
     * @param sql
     * @param params
     * @return
     */
    int[] update(String sql, Object[][] params);

    /**
     * single update.
     * @param sql
     * @param params
     * @return
     */
    int update(String sql, Object[] params);

    /**
     * 删除,删除可采用条件替代批量.
     * @param sql
     * @param params
     * @return
     */
    int delete(String sql, Object[] params);

}
