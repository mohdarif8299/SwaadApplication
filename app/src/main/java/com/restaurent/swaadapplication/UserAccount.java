package com.restaurent.swaadapplication;

public class UserAccount {
    private Address address;
    private PersonalDetails personalDetails;
    private Cart_Items cartItems;
    public UserAccount() { }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public Cart_Items getCartItems() {
        return cartItems;
    }

    public void setCartItems(Cart_Items cartItems) {
        this.cartItems = cartItems;
    }

    public UserAccount(Address address, PersonalDetails personalDetails, Cart_Items cartItems) {
        this.address = address;
        this.personalDetails = personalDetails;
        this.cartItems = cartItems;
    }
}
