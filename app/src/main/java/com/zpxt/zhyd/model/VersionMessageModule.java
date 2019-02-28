package com.zpxt.zhyd.model;

import com.google.gson.annotations.SerializedName;

/**
 * Description:      描述
 * Autour：          LF
 * Date：            2018/4/1 10:06
 */

public class VersionMessageModule {

    /**
     * resultDesc : null
     * resultData : null
     * total : 0
     * page : 0
     * rows : null
     * pageSize : 0
     * object : {"id":"1111111111111","name":"智慧用电","version":"1.2","appType":"0","downloadUrl":"https://www.pgyer.com/apiv2/app/install?appKey=f8ae29224aa9d76c723dc71de3071c16&_api_key=ae99cd8ded1a876f098050265b3564e2","apkUrl":"https://www.pgyer.com/apiv2/app/install?appKey=f8ae29224aa9d76c723dc71de3071c16&_api_key=ae99cd8ded1a876f098050265b3564e2","updateDesc":"1、测试版本更新","state":"RELEASE","releaseOrRevokeTime":1522552629000,"isUpdate":true,"createId":"1","createName":"admin","createTime":1522552543000,"delFlag":"0"}
     * sCount : 0
     * fCount : 0
     */

    @SerializedName("resultDesc")
    private Object resultDescX;
    private Object resultData;
    private int total;
    private int page;
    private Object rows;
    private int pageSize;
    private ObjectBean object;
    private int sCount;
    private int fCount;

    public Object getResultDescX() {
        return resultDescX;
    }

    public void setResultDescX(Object resultDescX) {
        this.resultDescX = resultDescX;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
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

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public int getSCount() {
        return sCount;
    }

    public void setSCount(int sCount) {
        this.sCount = sCount;
    }

    public int getFCount() {
        return fCount;
    }

    public void setFCount(int fCount) {
        this.fCount = fCount;
    }

    public static class ObjectBean {
        /**
         * id : 1111111111111
         * name : 智慧用电
         * version : 1.2
         * appType : 0
         * downloadUrl : https://www.pgyer.com/apiv2/app/install?appKey=f8ae29224aa9d76c723dc71de3071c16&_api_key=ae99cd8ded1a876f098050265b3564e2
         * apkUrl : https://www.pgyer.com/apiv2/app/install?appKey=f8ae29224aa9d76c723dc71de3071c16&_api_key=ae99cd8ded1a876f098050265b3564e2
         * updateDesc : 1、测试版本更新
         * state : RELEASE
         * releaseOrRevokeTime : 1522552629000
         * isUpdate : true
         * createId : 1
         * createName : admin
         * createTime : 1522552543000
         * delFlag : 0
         */

        private String id;
        private String name;
        private String version;
        private String appType;
        private String downloadUrl;
        private String apkUrl;
        private String updateDesc;
        private String state;
        private long releaseOrRevokeTime;
        private boolean isUpdate;
        private String createId;
        private String createName;
        private long createTime;
        private String delFlag;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAppType() {
            return appType;
        }

        public void setAppType(String appType) {
            this.appType = appType;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getApkUrl() {
            return apkUrl;
        }

        public void setApkUrl(String apkUrl) {
            this.apkUrl = apkUrl;
        }

        public String getUpdateDesc() {
            return updateDesc;
        }

        public void setUpdateDesc(String updateDesc) {
            this.updateDesc = updateDesc;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public long getReleaseOrRevokeTime() {
            return releaseOrRevokeTime;
        }

        public void setReleaseOrRevokeTime(long releaseOrRevokeTime) {
            this.releaseOrRevokeTime = releaseOrRevokeTime;
        }

        public boolean isIsUpdate() {
            return isUpdate;
        }

        public void setIsUpdate(boolean isUpdate) {
            this.isUpdate = isUpdate;
        }

        public String getCreateId() {
            return createId;
        }

        public void setCreateId(String createId) {
            this.createId = createId;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }
    }
}
