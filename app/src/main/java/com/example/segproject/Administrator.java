package com.example.segproject;

public class Administrator extends User{



    public Administrator(){

    }
    public Administrator(String firstName, String lastName, String password, String email){
        super("admin",firstName,lastName,password,email,"admin");

    }
}
