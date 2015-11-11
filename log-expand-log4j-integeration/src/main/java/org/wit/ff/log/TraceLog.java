package org.wit.ff.log;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by F.Fang on 2015/11/10.
 * Version :2015/11/10
 */
public class TraceLog {

    private String operation;

    /**
     * 任何一个操作都需要一个应用,此属性用于标识不同的应用数据接入.
     */
    private int appId;

    /**
     * 详细信息.
     */
    private String details;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
