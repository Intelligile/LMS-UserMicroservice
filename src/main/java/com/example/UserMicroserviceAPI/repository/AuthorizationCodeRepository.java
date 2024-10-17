package com.example.UserMicroserviceAPI.repository;

import com.example.UserMicroserviceAPI.model.AuthorizationCode;
import com.example.UserMicroserviceAPI.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, Long> {
    Optional<AuthorizationCode> findByLicenseeId(Long licenseeId); // New method to find by licenseeId

}
