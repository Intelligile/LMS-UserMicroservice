package com.example.UserMicroserviceAPI.dto;

import java.util.Set;

public class SignupRequest {

    private String username;

    private String firstname;

    private String lastname;

    private String phone;

    private String email;
    
    private String password;

    private boolean enabled;

    private Set<Long> authorityIDs;

    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setenabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Long> getAuthorityIDs() {
        return authorityIDs;
    }

    public void setAuthorityID(Set<Long> authorityIDs) {
        this.authorityIDs = authorityIDs;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
