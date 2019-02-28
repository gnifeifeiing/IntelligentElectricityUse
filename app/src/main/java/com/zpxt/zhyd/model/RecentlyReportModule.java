package com.zpxt.zhyd.model;

import java.io.Serializable;

/**
 * Description:      描述
 * Autour：          LF
 * Date：            2018/4/28 15:11
 */

public class RecentlyReportModule implements Serializable {

    private String nodeID;
    private String orgName;
    private String distributionBox;
    private String sensorName;
    private String data;
    private String generationTimedata;
    private String confirmState;

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDistributionBox() {
        return distributionBox;
    }

    public void setDistributionBox(String distributionBox) {
        this.distributionBox = distributionBox;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getGenerationTimedata() {
        return generationTimedata;
    }

    public void setGenerationTimedata(String generationTimedata) {
        this.generationTimedata = generationTimedata;
    }

    public String getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(String confirmState) {
        this.confirmState = confirmState;
    }
}
