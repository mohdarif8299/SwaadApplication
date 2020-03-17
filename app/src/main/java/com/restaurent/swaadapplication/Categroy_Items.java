package com.restaurent.swaadapplication;

public class Categroy_Items {
    String image,name;

    public Categroy_Items() {
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public Categroy_Items(String image, String name) {
        this.image = image;
        this.name = name;
    }
}
