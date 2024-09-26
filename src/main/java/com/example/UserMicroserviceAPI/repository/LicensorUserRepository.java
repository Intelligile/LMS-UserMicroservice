package com.example.UserMicroserviceAPI.repository;

import com.example.UserMicroserviceAPI.model.LicensorUser;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LicensorUserRepository extends JpaRepository<LicensorUser, Long> {
    Optional<LicensorUser> findByUsername(String username);
    Optional<LicensorUser> findByEmail(String email);

}

