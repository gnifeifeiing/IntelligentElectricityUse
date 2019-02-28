package com.zpxt.zhyd.model;

import java.io.Serializable;
import java.util.List;

/**
 * Description:      最近告警列表返回实体类
 * Autour：          LF
 * Date：            2018/3/28 14:22
 */

public class RecentlyReportListModule extends BaseModule{

    private String resultData;
    private int total;
    private int page;
    private int pageSize;
    private int sCount;
    private int fCount;
    private List<RecentlyReportModule> rows;

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

    public List<RecentlyReportModule> getRows() {
        return rows;
    }

    public void setRows(List<RecentlyReportModule> rows) {
        this.rows = rows;
    }
}
