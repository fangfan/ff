package org.wit.ff.jdbc.paging;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;
import org.wit.ff.jdbc.dialect.Dialect;
import org.wit.ff.jdbc.query.Criteria;
import org.wit.ff.jdbc.result.CriteriaResult;
import org.wit.ff.jdbc.result.CriteriaResultHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Yong.Huang
 * Updated by F.Fang on 2015/11/19.
 * Mybatis属于假分页 , 参考代码: DefaultResultSetHandler执行方法链:
 * handleResultSets--> handleResultSet --> handleRowValues --> handleRowValuesForNestedResultMap
 * --> skipRows --> 执行 rs.absolute跳过记录数, 实际执行的语句仍然是查询了相同数量的记录.
 */
public abstract class PagingInterceptor implements Interceptor {

    protected String statementRegex;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MetaObject metaObject = SystemMetaObject.forObject(invocation.getTarget());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        // 匹配拦截StatementId
        if (!mappedStatement.getId().matches(statementRegex)) {
            return invocation.proceed();
        }

        // 最后一个参数必须是Creteria.
        ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.resultSetHandler.parameterHandler");

        if (parameterHandler != null) {
            Object object = parameterHandler.getParameterObject();
            Object lastParam = null;
            // 如果有多个参数,取最后一个参数.
            if (object instanceof HashMap) {
                HashMap map = (HashMap) object;
                String key = "param"+String.valueOf(map.keySet().size()/2);
                lastParam = map.get(key);
            } else {
                lastParam = object;
            }

            // 参数一定要匹配Criteria类型
            if (lastParam == null || !(lastParam instanceof Criteria)) {
                return invocation.proceed();
            }

            Criteria criteria = (Criteria) lastParam;
            StatementHandler stamentHandler = (StatementHandler) invocation.getTarget();
            BoundSql boundSql = stamentHandler.getBoundSql();
            // 原始mapper文件中配置的Sql.
            String originSql = boundSql.getSql();

            int offSet = (criteria.getPageNumber() - 1) * criteria.getPageSize();
            // 实际的分页sql.
            String pagingSql = getDialect().getLimitString(originSql, offSet, criteria.getPageSize());

            // 重新设置属性.
            metaObject.setValue("delegate.boundSql.sql", pagingSql);
            metaObject.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
            metaObject.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

            // 获取连接参数.
            Connection connection = (Connection) invocation.getArgs()[0];
            // 总页数.
            int totalCount = getTotalCount(connection, originSql, parameterHandler);
            // 填充各属性值.
            CriteriaResult result = new CriteriaResult(criteria.getPageNumber(), criteria.getPageSize(), totalCount);
            CriteriaResultHolder.set(result);
        }
        // 执行SQL.
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        statementRegex = properties.getProperty("statementRegex");
    }

    public abstract Dialect getDialect();

    private int getTotalCount(Connection connection, String sql, ParameterHandler parameterHandler) throws SQLException {
        int result = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String countSql = getDialect().getCountString(sql);
            ps = connection.prepareStatement(countSql);
            parameterHandler.setParameters(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }
}
