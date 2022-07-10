package com.example.segproject;

public class Services {

    String serviceName;
    String id;
    // Booleans default to false
    boolean firstNameRequired;
    boolean lastNameRequired;
    boolean dateofbirthRequired;
    boolean addressRequired;
    boolean licenseTypeRequired;
    boolean proofOfStatusRequired;
    boolean proofOfResidenceRequired;
    boolean photoRequired;

    public Services(){

    }

    public Services(String id, String serviceName){
        this.serviceName = serviceName;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public boolean isProofOfStatusRequired() {
        return proofOfStatusRequired;
    }

    public void setProofOfStatusRequired(boolean proofOfStatusRequired) {
        this.proofOfStatusRequired = proofOfStatusRequired;
    }

    public boolean isProofofResidenceRequired() {
        return proofOfResidenceRequired;
    }

    public void setProofofResidenceRequired(boolean proofofResidenceRequired) {
        this.proofOfResidenceRequired = proofofResidenceRequired;
    }

    public boolean isPhotoRequired() {
        return photoRequired;
    }

    public void setPhotoRequired(boolean photoRequired) {
        this.photoRequired = photoRequired;
    }



    public String getServiceName() {
        return serviceName;
    }

    public boolean isFirstNameRequired() {
        return firstNameRequired;
    }

    public void setFirstNameRequired(boolean firstNameRequired) {
        this.firstNameRequired = firstNameRequired;
    }

    public boolean isLastNameRequired() {
        return lastNameRequired;
    }

    public void setLastNameRequired(boolean lastNameRequired) {
        this.lastNameRequired = lastNameRequired;
    }

    public boolean isDateofbirthRequired() {
        return dateofbirthRequired;
    }

    public void setDateofbirthRequired(boolean dateofbirthRequired) {
        this.dateofbirthRequired = dateofbirthRequired;
    }

    public boolean isAddressRequired() {
        return addressRequired;
    }

    public void setAddressRequired(boolean addressRequired) {
        this.addressRequired = addressRequired;
    }

    public boolean isLicenseTypeRequired() {
        return licenseTypeRequired;
    }

    public void setLicenseTypeRequired(boolean licenseTypeRequired) {
        this.licenseTypeRequired = licenseTypeRequired;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStringForm(){
        String description = "Required form info: \n";
        if(firstNameRequired){
            description += "First name, ";
        }
        if(lastNameRequired){
            description += "Last name, ";
        }
        if(addressRequired){
            description += "Address, ";
        }
        if(licenseTypeRequired){
            description += "License Type, ";
        }
        if(dateofbirthRequired){
            description += "Date of Birth, ";
        }
        return description.substring(0,description.length()-2);
    }

    public String getStringDocuments(){
        String description = "Required Documents:\n ";
        if(proofOfStatusRequired){
            description += "Proof of Status, ";
        }
        if(proofOfResidenceRequired){
            description += "Proof of Residence, ";
        }
        if(photoRequired){
            description += "Photo, ";
        }


        return  description.substring(0,description.length()-2);
    }
}
