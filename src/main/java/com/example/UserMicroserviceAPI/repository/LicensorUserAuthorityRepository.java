package com.example.UserMicroserviceAPI.repository;

import com.example.UserMicroserviceAPI.model.LicensorAuthority;
import com.example.UserMicroserviceAPI.model.LicensorUserAuthority;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface LicensorUserAuthorityRepository extends JpaRepository<LicensorUserAuthority, Long> {
    List<LicensorUserAuthority> findByUserId(Long userId);
    List<LicensorUserAuthority> findByAuthority(LicensorAuthority authority);

}
