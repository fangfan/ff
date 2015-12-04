package org.wit.fddl.sql;

/**
 * 表信息.
 * @author F.Fang
 *
 */
public class TableInfo {

    private String name;

    private String alias;

    public String getTableInfo() {
        return alias + SeperatorSymbol.POINT + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
