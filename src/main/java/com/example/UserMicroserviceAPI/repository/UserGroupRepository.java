package com.example.UserMicroserviceAPI.repository;
import com.example.UserMicroserviceAPI.model.UserGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    // Custom query methods (if needed)
}
