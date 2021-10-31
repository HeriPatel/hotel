package com.example.myapplication;

public class categoryModel {
    private String Categor_name;
    private String Categor_img;

    public categoryModel(String categor_name, String categor_img) {
        Categor_name = categor_name;
        Categor_img = categor_img;
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