package org.wit.ff.jdbc.access.dbutils;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.apache.commons.lang3.StringUtils;
import org.wit.ff.jdbc.converter.ParamsConverter;
import org.wit.ff.jdbc.access.IDataAccessor;
import org.wit.ff.jdbc.exception.DbUtilsDataAccessException;
import org.wit.ff.jdbc.id.IdGenerator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by F.Fang on 2015/3/31.
 * Version :2015/3/31
 */
public abstract class AbstractDataAccessor implements IDataAccessor {

    /**
     * 获取连接的方法时抽象的,目的是为了和事务绑定,开放Connection来源.
     * @return
     * @throws SQLException
     */
    protected abstract Connection getConnection() throws SQLException;

    /**
     * 获取连接的方法时抽象的,目的是为了和事务绑定,因为事务执行前后包含的sql逻辑可能不只一条, 不可在一条sql执行逻辑完成后关闭.
     * @param conn
     */
    protected abstract void closeConn(Connection conn);

    @Override
    public <T> List<T> query(String sql, Class<T> resultType){
        return query(sql, null, resultType);
    }

    /**
     * 查询时BeanListHandler将结果集转换成对象列表.
     * @param sql 查询语句
     * @param params 查询参数
     * @param resultType 返回类型
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> query(String sql, Object[] params, Class<T> resultType) {
        if (resultType == null || StringUtils.isBlank(sql)) {
            throw new DbUtilsDataAccessException("resultType can't be null and sql can't be blank!");
        }
        QueryRunner runner = new QueryRunner();
        Connection conn = null;
        List<T> result = null;
        try {
            conn = getConnection();
            if (params != null) {
                result = (List<T>) runner.query(conn, sql, new BeanListHandler(resultType), params);
            } else {
                result = (List<T>) runner.query(conn, sql, new BeanListHandler(resultType));
            }
        } catch (Exception e) {
            throw new DbUtilsDataAccessException("query error, sql is:" + sql, e);
        } finally {
            closeConn(conn);
        }
        return result;
    }

    /**
     *
     * @param sql insert语句.
     * @param params 对象参数列表.
     * @param paramsType 参数类型.
     * @param converter 参数转换器.
     * @param <T>
     */
    @Override
    public <T> void insert(String sql, List<T> params, Class<T> paramsType, ParamsConverter<T> converter) {
        if (StringUtils.isBlank(sql)) {
            throw new DbUtilsDataAccessException("sql can't be blank!");
        }
        if (params == null || params.isEmpty()) {
            throw new DbUtilsDataAccessException("params can't be null or empty!");
        }
        if (paramsType == null || converter == null) {
            throw new DbUtilsDataAccessException("paramsType or converter can't be null!");
        }

        QueryRunner runner = new QueryRunner();
        Connection conn = null;
        ArrayListHandler handler = new ArrayListHandler();
        List<Object[]> keys = null;
        try {
            conn = getConnection();
            int len = params.size();
            Object[][] arr = new Object[len][];
            for (int i = 0; i < len; ++i) {
                // 将单个对象处理成单行记录,用数组表示.
                arr[i] = converter.convert(params.get(i));
            }
            // 获取主键.
            keys = (List<Object[]>) runner.insertBatch(conn, sql, handler, arr);
            if (keys != null && keys.size() == len) {
                // implements IdGenerator.
                // 如果当前对象实现了IdGenerator接口, 则执行主键赋值的逻辑, 赋值逻辑由用户自定义在具体的对象类型当中
                if (params.get(0) instanceof IdGenerator) {
                    for (int i = 0; i < len; ++i) {
                        // 对每一个对象执行主键赋值(解析)
                        ((IdGenerator) params.get(i)).parseGenKey(keys.get(i));
                    }
                }
            }
        } catch (Exception e) {
            throw new DbUtilsDataAccessException("batch insert error, sql is:" + sql, e);
        }finally {
            closeConn(conn);
        }
    }

    /**
     * 批量插入.
     * @param sql
     * @param params
     */
    @Override
    public void insert(String sql, Object[][] params) {
        if(params==null || StringUtils.isBlank(sql)){
            throw new DbUtilsDataAccessException("params can't be null and sql can't be empty!");
        }
        QueryRunner runner = new QueryRunner();
        Connection conn = null;
        ArrayListHandler handler = new ArrayListHandler();
        try {
            conn = getConnection();
            runner.insertBatch(conn, sql, handler, params);
        } catch (Exception e) {
            throw new DbUtilsDataAccessException("batch insert error, sql is:" + sql, e);
        }finally {
            closeConn(conn);
        }
    }

    /**
     * 单条插入.
     * @param sql
     * @param params
     */
    @Override
    public void insert(String sql, Object[] params) {
        if(params==null || StringUtils.isBlank(sql)){
            throw new DbUtilsDataAccessException("params can't be null and sql can't be empty!");
        }
        QueryRunner runner = new QueryRunner();
        Connection conn = null;
        ArrayHandler handler = new ArrayHandler();
        try {
            conn = getConnection();
            runner.insert(conn, sql, handler, params);
        } catch (Exception e) {
            throw new DbUtilsDataAccessException("insert error, sql is:" + sql, e);
        }finally {
            closeConn(conn);
        }
    }

    /**
     * 批量更新.
     * @param sql
     * @param params
     * @return
     */
    @Override
    public int[] update(String sql, Object[][] params) {
        if(params==null || StringUtils.isBlank(sql)){
            throw new DbUtilsDataAccessException("params can't be null and sql can't be empty!");
        }
        QueryRunner runner = new QueryRunner();
        Connection conn = null;
        int[] result = null;
        try {
            conn = getConnection();
            result = runner.batch(conn, sql, params);
        } catch (Exception e) {
            throw new DbUtilsDataAccessException("batch update error, sql is:" + sql, e);
        }finally {
            closeConn(conn);
        }
        return result;
    }

    /**
     * 单条更新.
     * @param sql
     * @param params
     * @return
     */
    @Override
    public int update(String sql, Object[] params) {
        QueryRunner runner = new QueryRunner();
        Connection conn = null;
        int result = 0;
        try {
            conn = getConnection();
            if (params != null) {
                result = runner.update(conn, sql, params);
            } else {
                result = runner.update(conn, sql);
            }
        } catch (Exception e) {
            throw new DbUtilsDataAccessException("update error, sql is:" + sql, e);
        }finally {
            closeConn(conn);
        }
        return result;
    }

    /**
     * 删除,没有必要批量.
     * 即使是批量,也可以调用批量更新的方法.
     * 事实上此方法并无太大必要,只是为了避免歧义而已.
     * @param sql
     * @param params
     * @return
     */
    @Override
    public int delete(String sql, Object[] params) {
        return update(sql, params);
    }

}
