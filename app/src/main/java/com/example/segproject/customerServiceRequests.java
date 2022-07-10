package com.example.segproject;

public class customerServiceRequests {
    private String chosenDate;
    private String serviceName;
    private String firstName;
    private String lastName;
    private String DOB;
    private String address;
    private String licenseType;
    private serviceRequestStatus status;
    private String customerID;
    private String employeeID;
    private String branchName;
    // include documents  (images?) .........................

    public customerServiceRequests(){

    }



    public customerServiceRequests(String chosenDate, String serviceName, String firstName, String lastName, String DOB, String address, String licenseType, serviceRequestStatus status, String customerID,
                                   String employeeID, String branchName){
       this.chosenDate = chosenDate;
        this.serviceName = serviceName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.DOB = DOB;
        this.address = address;
        this.licenseType = licenseType;
        this.status= status;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getChosenDate() {
        return chosenDate;
    }

    public void setChosenDate(String chosenDate) {
        this.chosenDate = chosenDate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public serviceRequestStatus getStatus() {
        return status;
    }

    public void setStatus(serviceRequestStatus status) {
        this.status = status;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == this) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() == obj.getClass()) {
            customerServiceRequests cast = (customerServiceRequests) obj;
            if(this.chosenDate.equals(cast.getChosenDate()) && this.serviceName.equals(cast.getServiceName()) && this.firstName.equals(cast.getFirstName()) && this.lastName.equals(cast.getLastName()) &&
                    this.DOB.equals(cast.getDOB()) && this.address == cast.getAddress() && this.licenseType.equals(cast.getLicenseType()) && this.status == cast.getStatus() &&
                    this.customerID.equals(cast.getCustomerID()) && this.employeeID.equals(cast.getEmployeeID()) && this.serviceName.equals(cast.getServiceName())) {
                return true;
            }

        }
        return false;
    }



}
