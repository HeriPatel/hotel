package com.example.myapplication;

import java.io.Serializable;

public class itemsModel implements Serializable {
    private String id, name, price, des, img, qnty, itemNotes;

    public itemsModel(String id, String name, String price, String des, String img, String qnty, String itemNotes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.des = des;
        this.img = img;
        this.qnty = qnty;
        this.itemNotes = itemNotes;
    }

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getQnty() {
        return qnty;
    }

    public void setQnty(String qnty) {
        this.qnty = qnty;
    }

    public String getItemNotes() {
        return itemNotes;
    }

    public void setItemNotes(String itemNotes) {
        this.itemNotes = itemNotes;
    }
}