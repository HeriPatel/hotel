package com.example.myapplication;

public class subCategoryModel {
    private int subCatID;
    private String subCatName;
    private String subCatImg;

    public subCategoryModel(int subCatID, String subCatName, String subCatImg) {
        this.subCatID = subCatID;
        this.subCatName = subCatName;
        this.subCatImg = subCatImg;
    }

    public int getSubCatID() {
        return subCatID;
    }

    public void setSubCatID(int subCatID) {
        this.subCatID = subCatID;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getSubCatImg() {
        return subCatImg;
    }

    public void setSubCatImg(String subCatImg) {
        this.subCatImg = subCatImg;
    }
}