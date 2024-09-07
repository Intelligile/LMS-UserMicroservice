package com.example.UserMicroserviceAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.UserMicroserviceAPI.model.UserGroup;

@Repository
public interface GroupRepository extends JpaRepository<UserGroup, Long> {

    
}

