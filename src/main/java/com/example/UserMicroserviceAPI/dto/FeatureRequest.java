package com.example.UserMicroserviceAPI.dto;


import java.util.Date;

public class FeatureRequest {
    private String key;
    private Date keyExpiryDate;

    // Constructors
    public FeatureRequest() {}

    public FeatureRequest(String key, Date keyExpiryDate) {
        this.key = key;
        this.keyExpiryDate = keyExpiryDate;
    }

    // Getters and Setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getKeyExpiryDate() {
        return keyExpiryDate;
    }

    public void setKeyExpiryDate(Date keyExpiryDate) {
        this.keyExpiryDate = keyExpiryDate;
    }
}
