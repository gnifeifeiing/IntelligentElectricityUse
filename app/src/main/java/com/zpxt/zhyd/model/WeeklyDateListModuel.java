package com.zpxt.zhyd.model;

import java.util.List;

/**
 * Description:      检测周报日期列表
 * Autour：          LF
 * Date：            2018/3/30 10:58
 */

public class WeeklyDateListModuel  extends BaseModule{

    private String resultData;
    private int total;
    private int page;
    private int pageSize;
    private int sCount;
    private int fCount;
    private List<WeeklyDateModule> rows;

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

    public List<WeeklyDateModule> getRows() {
        return rows;
    }

    public void setRows(List<WeeklyDateModule> rows) {
        this.rows = rows;
    }

    public static class WeeklyDateModule{

//        "orgName" : "平煤七矿",
//        "num" : "1",
//        "treeParentIds" : "0$41$4104$pmsm$pmqk",
//        "startTime" : "2017-12-01",
//        "endTime" : "2017-12-08"

        private String orgName;
        private String num;
        private String treeParentIds;
        private String startTime;
        private String endTime;

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getTreeParentIds() {
            return treeParentIds;
        }

        public void setTreeParentIds(String treeParentIds) {
            this.treeParentIds = treeParentIds;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
