package com.adchina.dbutil.sqlhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.adchina.dbutil.condition.Condition;
import com.adchina.dbutil.datahandle.CaculateConditionResult;
import com.adchina.dbutil.datahandle.DataExecutor;
import com.adchina.dbutil.datahandle.DataModel;
import com.adchina.dbutil.datahandle.JdbcDataModel;
import com.adchina.dbutil.exception.NestedComponentException;
import com.adchina.dbutil.sql.parser.QuerySqlParser;
import com.adchina.dbutil.sql.tree.QuerySqlTree;

/**
 * 
 * @author F.Fang
 *
 */
public class QuerySqlJdbcHandler extends AbstractSqlHandler {
    
    private static final Log LOG = LogFactory.getLog(QuerySqlJdbcHandler.class);

    private JdbcTemplate jdbcTemplate;

    public QuerySqlJdbcHandler(List<String> newDbs, JdbcTemplate jdbcTemplate) {
        super(newDbs);
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * 执行查询.
     * @param sql
     * @param args
     * @return
     */
    public List<Map<String,Object>> query(final String sql, final Object[] args) {
     // (1)解析sql,构造SqlTree
        QuerySqlTree sqlTree = (QuerySqlTree)(new QuerySqlParser(sql).parse());

        // (2)获取执行语句.
        String executeSql = getExecuteSql(sqlTree);
        LOG.info("执行的语句为:" + executeSql);

        // (3)定义执行器.
        DataExecutor executor = getExecutor(executeSql, args);

        // (4)调用基类方法执行数据库查询过程和数据的再合并.
        Map<String, DataModel> dataContainer = handle(executor);

        // (5)执行目标条件,返回结果.
        // 目前条件为空,不执行条件过滤.
        // 最好返回数据收集器,可以重复利用已查询到的数据,求最大值,最小值都可以一次性拿到.
        // 执行排序和分组前 去掉无关排序列 去掉无关的分组列
        // 比如结果集合 count(id) 但是group by id,username,去掉username.
        // select count(id) from users order by id,username,
        // 执行完数据库sql后,排序结果集时username排序实际上也没有意义.
        return getTargetData(dataContainer, sqlTree.getCondition());
    }
    
    /**
     * 获取执行语句. 如果是单个db直接返回原始语句. 否则原始sql需要去掉 order by , group by 分页等条件之后的语句.
     * 
     * @param sqlTree
     * @return 具体数据库执行语句.
     */
    private String getExecuteSql(QuerySqlTree sqlTree) {
        // 如果是单个db直接返回原始语句
        // 否则原始sql需要去掉 order by , group by 分页等条件之后的语句.
        return isSingleDB() ? sqlTree.getOrignalSql() : sqlTree.getDbExecuteSql();
    }
    
    /**
     * 此方法暂时不抽象.
     * 
     * @param dataContainer
     * @param condition
     * @return
     */
    private List<Map<String, Object>> getTargetData(Map<String, DataModel> dataContainer, Condition condition) {
        // 若数据集为空,则返回null.
        if (dataContainer.isEmpty()) {
            return null;
        }

        // (1) 构造sql查询的结果集合列表
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Entry<String, DataModel> entry : dataContainer.entrySet()) {
            JdbcDataModel model = (JdbcDataModel) entry.getValue();
            // 只要一个任务执行发生异常,那么整体将抛出异常表示任务失败.
            if (model.isErrorFlag()) {
                throw new NestedComponentException("DB(" + entry.getKey() + ")查询任务失败," + model.getMsg());
            } else {
                LOG.info("DB(" + entry.getKey() + ")执行语句结果{" + model.getSqlExecuteInfo() + "}");
            }
            list.addAll(model.getData());
        }
        // (2) 循环结果集合,检索目标数据.
        // 如果数据只是从一个DB获取或数据列表为空,那么之后的所有条件都不需要执行.
        if (isSingleDB() || list.isEmpty()) {
            return list;
        } else {
            // 如果结果集不是来自于一个DB,那么之后的条件都需要继续过滤与分组.
            return new CaculateConditionResult().handleDataWithCondition(list, condition);
        }
    }

    @Override
    protected DataExecutor getExecutor(final String executeSql, final Object[] args) {
        return new DataExecutor() {
            @Override
            public JdbcDataModel execute() {
                // 接收任何sql语句的查询命令,均选择List<Map<String,Object>>作为结果集.
                JdbcDataModel dataModel = null;
                long start = System.nanoTime();
                try {
                    List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(executeSql, args);
                    long end = System.nanoTime();
                    // 使用ms来计算时间.
                    dataModel = new JdbcDataModel((end - start) / 1000000L);
                    dataModel.setData(resultSet);
                    dataModel.setCount(resultSet.size());
                } catch (Exception e) {
                    // 发生异常时,直接全局抓取异常,保证线程任务内的异常可以被截获.
                    long end = System.nanoTime();
                    dataModel = new JdbcDataModel((end - start), e.getMessage());
                }
                return dataModel;
            }
        };
    }

}
