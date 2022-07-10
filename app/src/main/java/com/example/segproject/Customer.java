package com.example.segproject;

public class Customer extends User{


    public Customer(){

    }
    public Customer(String UID, String firstName, String lastName, String password, String email){
        super(UID, firstName,lastName,password,email,"customer");

    }


}
