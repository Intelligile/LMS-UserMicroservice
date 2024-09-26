package com.example.UserMicroserviceAPI.model;

import java.util.HashSet;
import java.util.Set;

public class LicensorAuthorityDto {
    private long id;
    private String authority;
    private String description;
    private Set<Long> permissionIds = new HashSet<>(); // Initialize to an empty set
    private Set<Long> userIds = new HashSet<>(); // Initialize to an empty set

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(Set<Long> permissionIds) {
        this.permissionIds = permissionIds != null ? permissionIds : new HashSet<>(); // Avoid null
    }

    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> userIds) {
        this.userIds = userIds != null ? userIds : new HashSet<>(); // Avoid null
    }
}
