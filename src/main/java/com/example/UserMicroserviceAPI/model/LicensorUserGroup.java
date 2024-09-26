package com.example.UserMicroserviceAPI.model;

import jakarta.persistence.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class LicensorUserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

@ManyToMany(mappedBy = "LicensorGroups")
@JsonIgnore // Prevents recursive serialization of User in the UserGroup entity
private Set<LicensorUser> LicensorUsers;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<LicensorUser> getUsers() {
        return LicensorUsers;
    }

    public void setUsers(Set<LicensorUser> users) {
        this.LicensorUsers = users;
    }
}
