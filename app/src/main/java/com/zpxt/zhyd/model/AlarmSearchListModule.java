package com.zpxt.zhyd.model;

import java.util.List;

/**
 * Description:      搜索列表
 * Autour：          LF
 * Date：            2018/3/26 17:22
 */

public class AlarmSearchListModule  extends BaseModule{
    private String resultData;
    private int total;
    private int page;
    private int pageSize;
    private int sCount;
    private int fCount;
    private List<AlarmSearchModule> rows;

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

    public List<AlarmSearchModule> getRows() {
        return rows;
    }

    public void setRows(List<AlarmSearchModule> rows) {
        this.rows = rows;
    }

    public static class AlarmSearchModule{
        private String name;
        private String id;
        private String nodeType;
        private String parentId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNodeType() {
            return nodeType;
        }

        public void setNodeType(String nodeType) {
            this.nodeType = nodeType;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }
    }
}
