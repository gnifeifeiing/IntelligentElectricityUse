package com.zpxt.zhyd.model;

import java.io.Serializable;
import java.util.List;

/**
 * Description:      描述
 * Autour：          LF
 * Date：            2018/5/17 16:53
 */

public class MainFourthAlarmListMoudle  extends BaseModule{

    private String resultData;
    private int total;
    private int page;
    private int pageSize;
    private int sCount;
    private int fCount;
    private List<MainFourthAlarmModule> rows;

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

    public List<MainFourthAlarmModule> getRows() {
        return rows;
    }

    public void setRows(List<MainFourthAlarmModule> rows) {
        this.rows = rows;
    }

    public static class MainFourthAlarmModule implements Serializable {

        private String id;
        private String factorySn;
        private String gatewaySn;
        private String deviceSn;
        private String pointSn;
        private String pointTime;
        private String pointHour;
        private String pointValue;
        private String alarm;
        private String createTime;
        private String treatmentState;
        private String sortNo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFactorySn() {
            return factorySn;
        }

        public void setFactorySn(String factorySn) {
            this.factorySn = factorySn;
        }

        public String getGatewaySn() {
            return gatewaySn;
        }

        public void setGatewaySn(String gatewaySn) {
            this.gatewaySn = gatewaySn;
        }

        public String getDeviceSn() {
            return deviceSn;
        }

        public void setDeviceSn(String deviceSn) {
            this.deviceSn = deviceSn;
        }

        public String getPointSn() {
            return pointSn;
        }

        public void setPointSn(String pointSn) {
            this.pointSn = pointSn;
        }

        public String getPointTime() {
            return pointTime;
        }

        public void setPointTime(String pointTime) {
            this.pointTime = pointTime;
        }

        public String getPointHour() {
            return pointHour;
        }

        public void setPointHour(String pointHour) {
            this.pointHour = pointHour;
        }

        public String getPointValue() {
            return pointValue;
        }

        public void setPointValue(String pointValue) {
            this.pointValue = pointValue;
        }

        public String getAlarm() {
            return alarm;
        }

        public void setAlarm(String alarm) {
            this.alarm = alarm;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTreatmentState() {
            return treatmentState;
        }

        public void setTreatmentState(String treatmentState) {
            this.treatmentState = treatmentState;
        }

        public String getSortNo() {
            return sortNo;
        }

        public void setSortNo(String sortNo) {
            this.sortNo = sortNo;
        }
    }
}
