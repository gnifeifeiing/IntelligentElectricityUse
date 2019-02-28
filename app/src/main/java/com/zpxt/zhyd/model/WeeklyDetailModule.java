package com.zpxt.zhyd.model;

import java.util.List;

/**
 * Description:      检测周报返回实体类
 * Autour：          LF
 * Date：            2018/3/30 15:04
 */

public class WeeklyDetailModule extends BaseModule {

    private String resultData;
    private int total;
    private int page;
    private int pageSize;
    private int sCount;
    private int fCount;
    private List<WeeklyModule> rows;

    private ObjectModule object;

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

    public List<WeeklyModule> getRows() {
        return rows;
    }

    public void setRows(List<WeeklyModule> rows) {
        this.rows = rows;
    }

    public ObjectModule getObject() {
        return object;
    }

    public void setObject(ObjectModule object) {
        this.object = object;
    }

    public static class ObjectModule {
        /**
         * 最大报警点
         */
        private String maxNodeName;
        /**
         * 最大报警次数
         */
        private int maxNum;
        /**
         * 剩余电流报警次数
         */
        private int irNum;
        /**
         * 线缆电流报警次数
         */
        private int eleIaNum;
        /**
         * 线缆温度报警次数
         */
        private int eleTNum;
        /**
         * 箱体温度报警次数
         */
        private int txNum;
        /**
         * 报警总数
         */
        private int totalAlermCount;

        public int getTotalAlermCount() {
            return totalAlermCount;
        }

        public void setTotalAlermCount(int totalAlermCount) {
            this.totalAlermCount = totalAlermCount;
        }

        public String getMaxNodeName() {
            return maxNodeName;
        }

        public void setMaxNodeName(String maxNodeName) {
            this.maxNodeName = maxNodeName;
        }

        public int getMaxNum() {
            return maxNum;
        }

        public void setMaxNum(int maxNum) {
            this.maxNum = maxNum;
        }

        public int getIrNum() {
            return irNum;
        }

        public void setIrNum(int irNum) {
            this.irNum = irNum;
        }

        public int getEleIaNum() {
            return eleIaNum;
        }

        public void setEleIaNum(int eleIaNum) {
            this.eleIaNum = eleIaNum;
        }

        public int getEleTNum() {
            return eleTNum;
        }

        public void setEleTNum(int eleTNum) {
            this.eleTNum = eleTNum;
        }

        public int getTxNum() {
            return txNum;
        }

        public void setTxNum(int txNum) {
            this.txNum = txNum;
        }
    }

    public static class WeeklyModule {
        private String nodeName;
        /**
         * 剩余电流状态（0正常，1报警，2离线）
         */
        private String irNum;
        /**
         * 线缆电流状态（0正常，1报警，2离线）
         */
        private String eleIaNum;
        /**
         * 线缆温度状态（0正常，1报警，2离线）
         */
        private String eleTNum;
        /**
         * 箱体温度状态（0正常，1报警，2离线）
         */
        private String txNum;

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        public String getIrNum() {
            return irNum;
        }

        public void setIrNum(String irNum) {
            this.irNum = irNum;
        }

        public String getEleIaNum() {
            return eleIaNum;
        }

        public void setEleIaNum(String eleIaNum) {
            this.eleIaNum = eleIaNum;
        }

        public String getEleTNum() {
            return eleTNum;
        }

        public void setEleTNum(String eleTNum) {
            this.eleTNum = eleTNum;
        }

        public String getTxNum() {
            return txNum;
        }

        public void setTxNum(String txNum) {
            this.txNum = txNum;
        }
    }
}
