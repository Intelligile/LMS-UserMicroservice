package com.example.UserMicroserviceAPI.dto;

import java.util.HashSet;
import java.util.Set;

public class LicensorUserGroupDTO {
    private String name;
    private String description;
    private Set<Long> userIds = new HashSet<>(); // This will hold the IDs of the users to associate

    // Getters and Setters


    public String getName() {
        return name;
    }

    public void setName(String  name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String  description) {
        this.description = description;
    }
    
    public Set<Long> getUserGroupIds() {
        return userIds;
    }

    public void setUserGroupIds(Set<Long>  userIds) {
        this.userIds = userIds;
    }
}