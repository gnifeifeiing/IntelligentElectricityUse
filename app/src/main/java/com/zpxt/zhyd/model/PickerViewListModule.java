package com.zpxt.zhyd.model;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Description:      pickerview选择的list module
 * Autour：          LF
 * Date：            2018/3/26 17:33
 */

public class PickerViewListModule  implements IPickerViewData {
    private String name;
    private String id;
    private String pId;
    private String nodeType;

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }
}
