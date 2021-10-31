package com.example.myapplication;

public class subCategoryModel {
    private String subCatName;
    private String subCatImg;

    public subCategoryModel(String subCatName, String subCatImg) {
        this.subCatName = subCatName;
        this.subCatImg = subCatImg;
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