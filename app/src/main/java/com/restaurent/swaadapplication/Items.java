package com.restaurent.swaadapplication;

public class Items {
    private String item_name,item_category,item_image,item_type,item_status;
    private int item_price;
    public Items() {
    }
    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }
    public String getItem_status() {
        return item_status;
    }
    public Items(String item_name, String item_category, String item_image, String item_type, String item_status, int item_price) {
        this.item_name = item_name;
        this.item_category = item_category;
        this.item_image = item_image;
        this.item_type = item_type;
        this.item_status = item_status;
        this.item_price = item_price;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }
    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }
    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }
    public String getItem_name() {
        return item_name;
    }
    public String getItem_category() {
        return item_category;
    }
    public int getItem_price() {
        return item_price;
    }
    public String getItem_image() {
        return item_image;
    }
}
