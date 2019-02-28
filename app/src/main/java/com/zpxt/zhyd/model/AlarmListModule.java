package com.zpxt.zhyd.model;

import java.util.List;

/**
 * Description:      告警数据列表module
 * Autour：          LF
 * Date：            2018/3/26 14:25
 */

public class AlarmListModule extends BaseModule{

    private String resultData;
    private int total;
    private int page;
    private int pageSize;
    private int sCount;
    private int fCount;
    private List<AlarmModule> rows;

    public List<AlarmModule> getRows() {
        return rows;
    }

    public void setRows(List<AlarmModule> rows) {
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

    public static class AlarmModule{
        private String id;
        private String nodeName;
        /**
         * 如果此值为bottom表示下级是终极节点
         */
        private String nodeType;
        private String nodeCode;
        private String parentId;
        private String treeParentIds;
        private String sequ;
        private String addr;
        private String orgName;
        private String orgId;
        private String parentName;
        /**
         * 如果此值为null,表示非终极节点
         * 0：正常     1：告警    2：离线
         */
        private String state;
        private String alarmRate;
        private String normalRate;
        private String offlineRate;
        private String nodeNumbers;
        private String warningTime;
        private String routes;
        private String cType;

        public String getNodeNumbers() {
            return nodeNumbers;
        }

        public void setNodeNumbers(String nodeNumbers) {
            this.nodeNumbers = nodeNumbers;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        public String getNodeType() {
            return nodeType;
        }

        public void setNodeType(String nodeType) {
            this.nodeType = nodeType;
        }

        public String getNodeCode() {
            return nodeCode;
        }

        public void setNodeCode(String nodeCode) {
            this.nodeCode = nodeCode;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getTreeParentIds() {
            return treeParentIds;
        }

        public void setTreeParentIds(String treeParentIds) {
            this.treeParentIds = treeParentIds;
        }

        public String getSequ() {
            return sequ;
        }

        public void setSequ(String sequ) {
            this.sequ = sequ;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getAlarmRate() {
            return alarmRate;
        }

        public void setAlarmRate(String alarmRate) {
            this.alarmRate = alarmRate;
        }

        public String getNormalRate() {
            return normalRate;
        }

        public void setNormalRate(String normalRate) {
            this.normalRate = normalRate;
        }

        public String getOfflineRate() {
            return offlineRate;
        }

        public void setOfflineRate(String offlineRate) {
            this.offlineRate = offlineRate;
        }

        public String getWarningTime() {
            return warningTime;
        }

        public void setWarningTime(String warningTime) {
            this.warningTime = warningTime;
        }

        public String getRoutes() {
            return routes;
        }

        public void setRoutes(String routes) {
            this.routes = routes;
        }

        public String getcType() {
            return cType;
        }

        public void setcType(String cType) {
            this.cType = cType;
        }
    }

}
