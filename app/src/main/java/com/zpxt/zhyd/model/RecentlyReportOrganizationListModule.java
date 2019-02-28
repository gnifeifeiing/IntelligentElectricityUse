package com.zpxt.zhyd.model;

import java.util.List;

/**
 * Description:      最近告警列表--组织信息列表实体类
 * Autour：          LF
 * Date：            2018/3/28 14:37
 */

public class RecentlyReportOrganizationListModule extends BaseModule {

    private String resultData;
    private int total;
    private int page;
    private int pageSize;
    private int sCount;
    private int fCount;
    private List<RowsModule> rows;

    public List<RowsModule> getRows() {
        return rows;
    }

    public void setRows(List<RowsModule> rows) {
        this.rows = rows;
    }

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

    public static class RowsModule{
        private String orgID;
        private String orgName;

        public String getOrgID() {
            return orgID;
        }

        public void setOrgID(String orgID) {
            this.orgID = orgID;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }
    }
}
