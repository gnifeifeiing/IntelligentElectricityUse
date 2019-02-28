package com.zpxt.zhyd.model;

/**
 * Description:      实时数据处理意见返回实体类
 * Autour：          LF
 * Date：            2018/3/28 11:03
 */

public class RealTimeDealAdviceModule  extends BaseModule{
    private String resultData;
    private int total;
    private int page;
    private int pageSize;
    private int sCount;
    private int fCount;

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getsCount() {
        return sCount;
    }

    public void setsCount(int sCount) {
        this.sCount = sCount;
    }

    public int getfCount() {
        return fCount;
    }

    public void setfCount(int fCount) {
        this.fCount = fCount;
    }
}
