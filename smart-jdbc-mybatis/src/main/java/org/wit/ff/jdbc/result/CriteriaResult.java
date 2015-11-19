package org.wit.ff.jdbc.result;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Yong.Huang.
 * Updated by F.Fang on 2015/11/19.
 */
public class CriteriaResult{

    private int pageNumber;

    private int pageSize;

    private long pageCount;

    private long totalCount;

    public CriteriaResult(int pageNumber, int pageSize, long totalCount) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        if (pageSize != 0) {
            if (totalCount % pageSize == 0) {
                pageCount = totalCount / pageSize;
            } else {
                pageCount = totalCount / pageSize + 1;
            }
        }
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getPageCount() {
        return pageCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public boolean hasPrevPage() {
        return pageNumber > 1 && pageNumber <= pageCount;
    }

    public boolean hasNextPage() {
        return pageNumber < pageCount;
    }

    public boolean isFirstPage() {
        return pageNumber == 1;
    }

    public boolean isLastPage() {
        return pageNumber == pageCount;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
