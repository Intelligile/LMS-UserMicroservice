package com.example.UserMicroserviceAPI.repository;

import com.example.UserMicroserviceAPI.model.Authority;
import com.example.UserMicroserviceAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.UserMicroserviceAPI.model.UserAuthority;

import java.util.List;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
    List<UserAuthority> findByUserId(Long userId);
    List<UserAuthority> findByAuthority(Authority authority);

}
