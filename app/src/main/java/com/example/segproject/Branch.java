package com.example.segproject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Branch {

    private String phoneNumber;
    private String branchName;
    private String address;
    private String postal;
    private ArrayList<ArrayList<Integer>> workingHours;
    private ArrayList<Services> branchServices = new ArrayList<Services>();
    private ArrayList<customerRatings> ratedBy;
    private double branchRating;
    private ArrayList<customerServiceRequests> serviceRequests;
    public Branch(){

    }




    public Branch(String phoneNumber, String postal, String branchName, String address, ArrayList<ArrayList<Integer>> workingHours, ArrayList<Services> branchServices,
                  ArrayList<customerRatings> ratedBy, double branchRating, ArrayList<customerServiceRequests> serviceRequests){
        this.phoneNumber = phoneNumber;
        this.postal = postal;
        this.branchName = branchName;
        this.address = address;
        this.workingHours = workingHours;
        this.branchServices = branchServices;
        this.branchRating = branchRating;
        this.ratedBy = ratedBy;
        this.serviceRequests = serviceRequests;


    }

    public ArrayList<customerServiceRequests> getServiceRequests() {
        return serviceRequests;
    }

    public void setServiceRequests(ArrayList<customerServiceRequests> serviceRequests) {
        this.serviceRequests = serviceRequests;
    }

    public ArrayList<customerRatings> getRatedBy() {
        return ratedBy;
    }

    public void setRatedBy(ArrayList<customerRatings> ratedBy) {
        this.ratedBy = ratedBy;
    }

    public double getBranchRating() {
        return branchRating;
    }

    public void setBranchRating(double branchRating) {
        this.branchRating = branchRating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public ArrayList<ArrayList<Integer>> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(ArrayList<ArrayList<Integer>> workingHours) {
        this.workingHours = workingHours;
    }

    public ArrayList<Services> getBranchServices() {
        return branchServices;
    }

    public void setBranchServices(ArrayList<Services> branchServices) {
        this.branchServices = branchServices;
    }
}
