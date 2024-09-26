package com.example.UserMicroserviceAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.UserMicroserviceAPI.model.LicensorUserGroup;
@Repository
public interface LicensorGroupRepository extends JpaRepository<LicensorUserGroup, Long> {

    
}

