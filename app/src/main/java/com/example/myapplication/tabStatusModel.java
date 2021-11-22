package com.example.myapplication;

public class tabStatusModel {
    private int tabID;
    private String tabName;
    private String tabStatus;

    public tabStatusModel(int tabID, String tabName, String tabStatus) {
        this.tabID = tabID;
        this.tabName = tabName;
        this.tabStatus = tabStatus;
    }

    public int getTabID() {
        return tabID;
    }

    public void setTabID(int tabID) {
        this.tabID = tabID;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getTabStatus() {
        return tabStatus;
    }

    public void setTabStatus(String tabStatus) {
        this.tabStatus = tabStatus;
    }
}