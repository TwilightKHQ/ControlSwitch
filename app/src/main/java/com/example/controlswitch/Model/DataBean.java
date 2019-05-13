package com.example.controlswitch.Model;

public class DataBean {

    public static final int PARENT_ITEM = 0;//父布局
    public static final int CHILD_ITEM = 1;//子布局

    private int type;// 显示类型
    private boolean isExpand;// 是否展开
    private DataBean childBean;

    private String Port; //端口号
    private String switchName; //开关名称
    private String switchLocation; //开关位置
    private String parentIntroduction; //父布局当中的开关介绍
    private String childIntroduction; //子布局当中的开关介绍

    private boolean isOn = false;

    public boolean getIsOn() {
        return isOn;
    }

    public void setIsOn(boolean IsOn) {
        this.isOn = IsOn;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getChildIntroduction() {
        return childIntroduction;
    }

    public void setChildIntroduction(String childIntroduction) {
        this.childIntroduction = childIntroduction;
    }

    public String getParentIntroduction() {
        return parentIntroduction;
    }

    public void setParentIntroduction(String parentIntroduction) {
        this.parentIntroduction = parentIntroduction;
    }

    public String getSwitchLocation() {
        return switchLocation;
    }

    public void setSwitchLocation(String switchLocation) {
        this.switchLocation = switchLocation;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public DataBean getChildBean() {
        return childBean;
    }

    public void setChildBean(DataBean childBean) {
        this.childBean = childBean;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        this.Port = port;
    }
}
