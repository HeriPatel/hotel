package com.example.myapplication;

public class categoryModel {
    private int category_id;
    private String Categor_name;
    private String Categor_img;

    public categoryModel(int category_id, String categor_name, String categor_img) {
        this.category_id = category_id;
        Categor_name = categor_name;
        Categor_img = categor_img;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategor_name() {
        return Categor_name;
    }

    public void setCategor_name(String categor_name) {
        Categor_name = categor_name;
    }

    public String getCategor_img() {
        return Categor_img;
    }

    public void setCategor_img(String categor_img) {
        Categor_img = categor_img;
    }
}