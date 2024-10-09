package com.example.UserMicroserviceAPI.repository;
import com.example.UserMicroserviceAPI.model.LicensorUser;
import com.example.UserMicroserviceAPI.model.LicensorUserGroup;


import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LicensorUserGroupRepository extends JpaRepository<LicensorUserGroup, Long> {
    // Custom query methods (if needed)
    @Modifying
    @Transactional
    @Query("DELETE FROM LicensorUserGroup ug WHERE ug.id = :groupId AND :users MEMBER OF ug.LicensorUsers")
    void removeUsersFromGroup(@Param("groupId") Long groupId, @Param("users") List<LicensorUser> users);
    
}


