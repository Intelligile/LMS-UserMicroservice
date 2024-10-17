package com.example.UserMicroserviceAPI.dto;

public class LoginResponse {

    //response structure
    private String jwtToken;
    private String username;
    private String roles;
    private boolean isLicensor;  // New field

    public LoginResponse(String username, String roles, String jwtToken, boolean isLicensor) {
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;
        this.isLicensor = isLicensor;  // Assign the value
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean getIsLicensor() {
        return isLicensor;
    }

    public void setIsLicensor(boolean isLicensor) {
        this.isLicensor = isLicensor;
    }
}