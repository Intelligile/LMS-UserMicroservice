package com.example.UserMicroserviceAPI.repository;
import com.example.UserMicroserviceAPI.model.UserGroup;
import com.example.UserMicroserviceAPI.model.User;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    // Custom query methods (if needed)
    @Modifying
    @Transactional
    @Query("DELETE FROM UserGroup ug WHERE ug.id = :groupId AND ug IN :users")
    void removeUsersFromGroup(@Param("groupId") Long groupId, @Param("users") List<User> users);
    
}


