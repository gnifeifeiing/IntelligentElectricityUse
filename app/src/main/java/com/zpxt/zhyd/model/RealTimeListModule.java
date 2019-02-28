package com.zpxt.zhyd.model;

import java.util.List;

/**
 * Description:      描述
 * Autour：          LF
 * Date：            2018/3/27 18:20
 */

public class RealTimeListModule  extends BaseModule{

    private String resultData;
    private int total;
    private int page;
    private int pageSize;
    private int sCount;
    private int fCount;
    private List<RealTimeDetailModule> rows;

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

    public List<RealTimeDetailModule> getRows() {
        return rows;
    }

    public void setRows(List<RealTimeDetailModule> rows) {
        this.rows = rows;
    }

    public static class RealTimeDetailModule{

        private String POINTID;
        private String ALARM;
        private String TIME;
        private String SN;
        private String VALUE;
        private String NAME;

        public String getPOINTID() {
            return POINTID;
        }

        public void setPOINTID(String POINTID) {
            this.POINTID = POINTID;
        }

        public String getALARM() {
            return ALARM;
        }

        public void setALARM(String ALARM) {
            this.ALARM = ALARM;
        }

        public String getTIME() {
            return TIME;
        }

        public void setTIME(String TIME) {
            this.TIME = TIME;
        }

        public String getSN() {
            return SN;
        }

        public void setSN(String SN) {
            this.SN = SN;
        }

        public String getVALUE() {
            return VALUE;
        }

        public void setVALUE(String VALUE) {
            this.VALUE = VALUE;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }
    }
}