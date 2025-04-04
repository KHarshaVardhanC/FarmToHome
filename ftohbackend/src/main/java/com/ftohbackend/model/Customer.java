package com.ftohbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String customerPassword;
    private String customerLocation;
    private String customerPhoneNumber;
    private Boolean customerIsActive;

    public Customer() {
        super();
    }

    public Customer(Integer customerId, String customerFirstName, String customerLastName,
                    String customerEmail, String customerPassword, String customerLocation,
                    String customerPhoneNumber, Boolean customerIsActive) {
        super();
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerEmail = customerEmail;
        this.customerPassword = customerPassword;
        this.customerLocation = customerLocation;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerIsActive = customerIsActive;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public Boolean getCustomerIsActive() {
        return customerIsActive;
    }

    public void setCustomerIsActive(Boolean customerIsActive) {
        this.customerIsActive = customerIsActive;
    }

    @Override
    public String toString() {
        return "Customer [customerId=" + customerId + ", customerFirstName=" + customerFirstName
                + ", customerLastName=" + customerLastName + ", customerEmail=" + customerEmail + ", customerPassword="
                + customerPassword + ", customerLocation=" + customerLocation + ", customerPhoneNumber="
                + customerPhoneNumber + ", customerIsActive=" + customerIsActive + "]";
    }
}
