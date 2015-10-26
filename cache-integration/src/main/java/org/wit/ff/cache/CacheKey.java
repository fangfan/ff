package org.wit.ff.cache;

/**
 * Created by F.Fang on 2015/10/23.
 * Version :2015/10/23
 */
public class CacheKey {

    /**
     * 方法签名.
     */
    private String method;

    /**
     * 接口参数.
     */
    private Object[] params;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
