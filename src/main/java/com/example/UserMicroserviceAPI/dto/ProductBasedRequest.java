package com.example.UserMicroserviceAPI.dto;

import com.example.UserMicroserviceAPI.model.User;

public class ProductBasedRequest {

    private int productLimit;
    private Long licenseeId;
    private Long productId; // Assuming a Product entity exists
    private double discount; // Discount applicable
    // Getters and setters
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
