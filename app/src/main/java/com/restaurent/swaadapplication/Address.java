package com.restaurent.swaadapplication;

public class Address {
    private String house,pincode,landmark;
    public Address(String house, String pincode, String landmark) {
        this.house = house;
        this.pincode = pincode;
        this.landmark = landmark;
    }
    public Address() { }
    public String getHouse() { return house; }
    public void setHouse(String house) { this.house = house; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }
}
