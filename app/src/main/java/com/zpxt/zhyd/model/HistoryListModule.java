package com.zpxt.zhyd.model;

import java.io.Serializable;
import java.util.List;

/**
 * Description:      历史数据列表
 * Autour：          LF
 * Date：            2018/3/29 14:24
 */

public class HistoryListModule extends BaseModule{

    private String resultData;
    private int total;
    private int page;
    private int pageSize;
    private int sCount;
    private int fCount;
    private List<HistoryModule> rows;

    private ObjectModule object;

    public ObjectModule getObject() {
        return object;
    }

    public void setObject(ObjectModule object) {
        this.object = object;
    }

    public static class ObjectModule implements Serializable{
        private List<CurrentModule> currentList;

        private List<TemperatureListModule> temperatureList;


        public List<CurrentModule> getCurrentList() {
            return currentList;
        }

        public void setCurrentList(List<CurrentModule> currentList) {
            this.currentList = currentList;
        }

        public List<TemperatureListModule> getTemperatureList() {
            return temperatureList;
        }

        public void setTemperatureList(List<TemperatureListModule> temperatureList) {
            this.temperatureList = temperatureList;
        }

        public static class CurrentModule implements Serializable{
            private String 剩余电流;
            private String A相电流;
            private String B相电流;
            private String C相电流;
            private String time;

            public String get剩余电流() {
                return 剩余电流;
            }

            public void set剩余电流(String 剩余电流) {
                this.剩余电流 = 剩余电流;
            }

            public String getA相电流() {
                return A相电流;
            }

            public void setA相电流(String a相电流) {
                A相电流 = a相电流;
            }

            public String getB相电流() {
                return B相电流;
            }

            public void setB相电流(String b相电流) {
                B相电流 = b相电流;
            }

            public String getC相电流() {
                return C相电流;
            }

            public void setC相电流(String c相电流) {
                C相电流 = c相电流;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public static class TemperatureListModule implements Serializable{
            private String 箱体温度;
            private String A相温度;
            private String B相温度;
            private String C相温度;
            private String time;

            public String get箱体温度() {
                return 箱体温度;
            }

            public void set箱体温度(String 箱体温度) {
                this.箱体温度 = 箱体温度;
            }

            public String getA相温度() {
                return A相温度;
            }

            public void setA相温度(String a相温度) {
                A相温度 = a相温度;
            }

            public String getB相温度() {
                return B相温度;
            }

            public void setB相温度(String b相温度) {
                B相温度 = b相温度;
            }

            public String getC相温度() {
                return C相温度;
            }

            public void setC相温度(String c相温度) {
                C相温度 = c相温度;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
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

    public List<HistoryModule> getRows() {
        return rows;
    }

    public void setRows(List<HistoryModule> rows) {
        this.rows = rows;
    }

    public static class HistoryModule{
        private String id;
        private String factorySn;
        private String gatewaySn;
        private String deviceSn;
        private String pointSn;
        private String pointTime;
        private String pointHour;
        private String pointValue;
        private String createTime;
        private String nodeName;
        private String sensorName;

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        public String getSensorName() {
            return sensorName;
        }

        public void setSensorName(String sensorName) {
            this.sensorName = sensorName;
        }
    }
}
