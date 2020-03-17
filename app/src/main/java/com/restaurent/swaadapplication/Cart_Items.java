package com.restaurent.swaadapplication;

public class Cart_Items {
    private String item_name,item_category,item_status,item_image;
    private int quantity,price,totalPrice;
    public void settotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public int gettotalPrice() { return totalPrice; }
    public Cart_Items(String item_name, String item_category, String item_status, String item_image, int quantity, int price, int totalPrice) {
        this.item_name = item_name;
        this.item_category = item_category;
        this.item_status = item_status;
        this.item_image = item_image;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }
    public Cart_Items() { }
    public void setItem_name(String item_name) { this.item_name = item_name; }
    public void setItem_category(String item_category) { this.item_category = item_category; }
    public void setItem_status(String item_status) { this.item_status = item_status; }
    public void setItem_image(String item_image) { this.item_image = item_image; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(int price) { this.price = price; }
    public String getItem_name() { return item_name; }
    public String getItem_category() { return item_category; }
    public String getItem_status() { return item_status; }
    public String getItem_image() { return item_image; }
    public int getQuantity() { return quantity; }
    public int getPrice() { return price; }
}
