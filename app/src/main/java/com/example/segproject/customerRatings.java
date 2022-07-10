package com.example.segproject;

public class customerRatings {
    private String customerID;
    private float customerRating;
    public customerRatings(){

    }
    public customerRatings(String customerID, float customerRating){
        this.customerID = customerID;
        this.customerRating = customerRating;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public float getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(float customerRating) {
        this.customerRating = customerRating;
    }
}
