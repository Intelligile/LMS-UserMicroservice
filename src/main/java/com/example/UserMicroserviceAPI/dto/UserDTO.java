package com.example.UserMicroserviceAPI.dto;
import com.example.UserMicroserviceAPI.model.Authority;
import com.example.UserMicroserviceAPI.model.UserGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class UserDTO {
    private Long id;

    private String username;
    private String email;
    private boolean enabled;
    private String firstname;
    private String lastname;
    private String phone;

    @JsonIgnore
    private List<Authority> authorities;

    @JsonIgnore
    private List<UserGroup> groups;

    // Constructor
    public UserDTO(Long id, String username, String email, String firstname, String lastname, String phone, boolean enabled,
                   List<Authority> authorities, List<UserGroup> groups) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.enabled = enabled;
        this.authorities = authorities;
        this.groups = groups;
    }

    // Default constructor
    public UserDTO() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public List<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<UserGroup> groups) {
        this.groups = groups;
    }
}
