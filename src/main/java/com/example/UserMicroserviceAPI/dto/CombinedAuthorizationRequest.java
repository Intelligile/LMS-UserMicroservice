package com.example.UserMicroserviceAPI.dto;

import com.example.UserMicroserviceAPI.model.User;

public class CombinedAuthorizationRequest {

    private double amount;
    private int periodMonths;
    private double totalCredit;
    private int productLimit;
    private Long licenseeId;
    private Long productId; // Assuming a Product entity exists
    private double discount; // Discount applicable
    // Getters and setters
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPeriodMonths() {
        return periodMonths;
    }

    public void setPeriodMonths(int periodMonths) {
        this.periodMonths = periodMonths;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public int getProductLimit() {
        return productLimit;
    }

    public void setProductLimit(int productLimit) {
        this.productLimit = productLimit;
    }

    public Long getLicenseeId() {
        return licenseeId;
    }

    public void setLicenseeId(Long licenseeId) {
        this.licenseeId = licenseeId;
    }
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
