package com.example.UserMicroserviceAPI.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    // @ManyToMany(mappedBy = "groups")
    // private Set<User> users;

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

    // public Set<User> getUsers() {
    //     return users;
    // }

    // public void setUsers(Set<User> users) {
    //     this.users = users;
    // }
}
