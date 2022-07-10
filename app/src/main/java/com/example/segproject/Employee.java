package com.example.segproject;

public class Employee extends User {

    public Branch branch;
    public Employee(){

    }
    public Employee(String UID, String firstName, String lastName, String password, String email, Branch branch){
        super(UID, firstName,lastName,password,email,"employee");
        this.branch = branch;

    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
