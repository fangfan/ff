package org.wit.ff.jdbc.query;


/**
 * Created by F.Fang on 2015/11/19.
 * 后续可扩展排序参数.
 */
public class Criteria {

    private int pageNumber;

    private int pageSize;

    public Criteria page(int pageNumber, int pageSize){
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        return this;
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
