#feature#
**分布式数据库查询组件(起始版本)**
- 暂只支持查询。
- 能够查询一台或多台数据库。
- 可以在配置文件中指定一组连接字符串，也可以在程序中通过参数来指定。
- 暂时只支持SELECT查询。
- 支持DISTINCT、WHERE、GROUP BY、ORDER BY、分页。
- 支持聚合函数：COUNT、SUM、MAX、MIN。
- 支持MySQL、Oracle、SqlServer数据库，暂时只支持MySQL。
- 不支持子查询。
- 不支持聚合函数内部DISTINCT。
- 不支持使用的表的别名。
- 不支持SELECT *这样的写法，必须明确给出要查询的字段。
