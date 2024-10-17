package com.example.UserMicroserviceAPI.dto;

import java.util.Date;
import java.util.List;

public class LicenseRequest {

    private Long userId;
    private Long licenseeUserId;
    private String deviceName;
    private String serverCode;
    private Date endDate;
    private List<FeatureRequest> features;

    // Headers for communication
    private String authorizationToken;
    private String clientId;

    // Constructors
    public LicenseRequest() {}

    public LicenseRequest(Long userId,Long licenseeUserId, String deviceName, String serverCode, Date endDate, List<FeatureRequest> features, String authorizationToken, String clientId) {
        this.userId = userId;
        this.licenseeUserId = licenseeUserId;
        this.deviceName = deviceName;
        this.serverCode = serverCode;
        this.endDate = endDate;
        this.features = features;
        this.authorizationToken = authorizationToken;
        this.clientId = clientId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getLicenseeUserId() {
        return licenseeUserId;
    }

    public void setLicenseeUserId(Long licenseeUserId) {
        this.licenseeUserId = licenseeUserId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<FeatureRequest> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureRequest> features) {
        this.features = features;
    }

    // Header-related fields
    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "LicenseRequest{" +
                "userId=" + userId +
                "licenseeUserId=" + licenseeUserId +
                ", deviceName='" + deviceName + '\'' +
                ", serverCode='" + serverCode + '\'' +
                ", endDate=" + endDate +
                ", features=" + features +
                ", authorizationToken='" + authorizationToken + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}