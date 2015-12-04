package org.wit.fddl.sql.keys;

import org.wit.fddl.sql.AggregationType;
import org.wit.fddl.sql.SeperatorSymbol;
import org.wit.fddl.sql.SqlKeyWords;

public class SelectSqlKey extends SqlKey {

    /**
     * 聚合函数类型.
     */
    private AggregationType type;

    /**
     * 是否拥有as 标识. t.id as tid;
     */
    private boolean hasAs;

    /**
     * 键别名. tid t.id as tid;
     */
    private String aliasKey;
    
    public String getSqlSegment(){
        StringBuffer sb = new StringBuffer();
        if (type != null) {
            sb.append(type.name()).append(
                            SeperatorSymbol.LEFT_PARENTHESIS + getKey() + SeperatorSymbol.RIGHT_PARENTHESIS);
        } else {
            sb.append(getKey());
        }
        if(hasAs){
            sb.append(SeperatorSymbol.EMPTY);
            sb.append(SqlKeyWords.AS);
            sb.append(SeperatorSymbol.EMPTY);
            sb.append(aliasKey);
        }
        return sb.toString();
    }

    /**
     * 获取结果集key.
     * 查找数据集当中字段的名称. 如果字段没有被聚合函数包装,直接是field 例如:select id,name
     * from users; 结果列表单条记录集合 map 使用map.get("id")取值.
     * 如果字段被聚合函数包装,以max为例,名称为max(groupByField) 例如:select id,max(name) from users
     * group by id; 结果列表单条记录集合 map 使用map.get("max(name)")取值.
     * 如果是别名,直接使用别名取值.
     * @return
     */
    public String getResultSetKey() {
        if (hasAs) {
            return aliasKey;
        } else {
            StringBuffer sb = new StringBuffer();
            if (type != null) {
                sb.append(type.name()).append(
                                SeperatorSymbol.LEFT_PARENTHESIS + getKey() + SeperatorSymbol.RIGHT_PARENTHESIS);
            } else {
                sb.append(getKey());
            }
            return sb.toString();
        }
    }

    public String getKey() {
        if (tbAlias != null) {
            return tbAlias + SeperatorSymbol.POINT + key;
        }
        return key;
    }

    public AggregationType getType() {
        return type;
    }

    public void setType(AggregationType type) {
        this.type = type;
    }

    public boolean isHasAs() {
        return hasAs;
    }

    public void setHasAs(boolean hasAs) {
        this.hasAs = hasAs;
    }

    public String getAliasKey() {
        return aliasKey;
    }

    public void setAliasKey(String aliasKey) {
        this.aliasKey = aliasKey;
    }

}
