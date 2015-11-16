package org.wit.ff.jdbc.core;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;
import org.wit.ff.jdbc.converter.ParamsConverter;
import org.wit.ff.jdbc.exception.DbUtilsDataAccessException;
import org.wit.ff.jdbc.id.IdGenerator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by F.Fang on 2015/3/31.
 * Version :2015/3/31
 */
public abstract class AbstractDataAccessor implements IDataAccessor {

    protected abstract Connection getConnection() throws SQLException;

    protected abstract void closeConn(Connection conn);

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

    @Override
    public List<Map<String, Object>> queryMap(String sql, Object[] params) {
        if (StringUtils.isBlank(sql)) {
            throw new DbUtilsDataAccessException("sql can't be blank!");
        }
        QueryRunner runner = new QueryRunner();
        Connection conn = null;
        List<Map<String, Object>> result = null;
        MapListHandler handler = new MapListHandler();
        try {
            conn = getConnection();
            if (params != null) {
                result = runner.query(conn, sql, handler, params);
            } else {
                result = runner.query(conn, sql, handler);
            }
        } catch (Exception e) {
            throw new DbUtilsDataAccessException("queryMap error, sql is:" + sql, e);
        } finally {
            closeConn(conn);
        }
        return result;
    }

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
                arr[i] = converter.convert(params.get(i));
            }
            keys = (List<Object[]>) runner.insertBatch(conn, sql, handler, arr);
            if (keys != null && keys.size() == len) {
                // implements IdGenerator.
                if (params.get(0) instanceof IdGenerator) {
                    for (int i = 0; i < len; ++i) {
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

    @Override
    public int delete(String sql, Object[] params) {
        return update(sql, params);
    }

}
