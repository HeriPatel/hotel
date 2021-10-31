package com.example.myapplication;

public class tabStatusModel {
    private String tabName;
    private String tabStatus;

    public tabStatusModel(String tabName, String tabStatus) {
        this.tabName = tabName;
        this.tabStatus = tabStatus;
    }

    public String getTabName() {
        return tabName;
    }

    public String getTabStatus() {
        return tabStatus;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public void setTabStatus(String tabStatus) {
        this.tabStatus = tabStatus;
    }
}