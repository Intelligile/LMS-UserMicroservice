
package com.example.UserMicroserviceAPI.repository;

import com.example.UserMicroserviceAPI.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByAuthorities_Authority(String authorityName);
}
