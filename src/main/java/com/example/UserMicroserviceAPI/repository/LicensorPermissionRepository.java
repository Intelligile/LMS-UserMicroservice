
package com.example.UserMicroserviceAPI.repository;

import com.example.UserMicroserviceAPI.model.LicensorPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicensorPermissionRepository extends JpaRepository<LicensorPermission, Long> {
    List<LicensorPermission> findByAuthorities_Authority(String authorityName);
}
