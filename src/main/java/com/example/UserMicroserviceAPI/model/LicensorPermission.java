
package com.example.UserMicroserviceAPI.model;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.ManyToMany;


import java.util.HashSet;
import java.util.Set;


@Entity
public class LicensorPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String permission;
    private String description;

    @ManyToMany(mappedBy = "LicensorPermissions")
    private Set<LicensorAuthority> authorities = new HashSet<>();;

    // Getters and setters
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
        return description;
    }

    public void setPermissioinDescription(String description) {
        this.description = description;
    }

    public Set<LicensorAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<LicensorAuthority> authorities) {
        this.authorities = authorities;
    }
}
