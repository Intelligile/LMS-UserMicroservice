package com.example.UserMicroserviceAPI.model;

import java.util.Set;

public class LicensorPermissionDto {
    private Long id;
    private String permission;
    private String permissionDescription;
    private Set<Long> authorityIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermissionDescription() {
        return permissionDescription;
    }

    public void setPermissionDescription(String permissionDescription) {
        this.permissionDescription = permissionDescription;
    }

    public Set<Long> getAuthorityIds() {
        return authorityIds;
    }

    public void setAuthorityIds(Set<Long> authorityIds) {
        this.authorityIds = authorityIds;
    }
}
