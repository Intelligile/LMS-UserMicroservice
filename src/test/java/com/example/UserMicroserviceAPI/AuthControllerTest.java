package com.example.UserMicroserviceAPI;

import com.example.UserMicroserviceAPI.controller.AuthController;
import com.example.UserMicroserviceAPI.dto.LoginRequest;
import com.example.UserMicroserviceAPI.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void contextLoads() throws Exception {
        assertThat(authController).isNotNull();
    }

    @Test
    public void testAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("fadelzohbi");
        loginRequest.setPassword("12345");

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserDetails userDetails = userDetailsService.loadUserByUsername("fadelzohbi");
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        assertThat(jwtToken).isNotNull();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery("SELECT username, password, enabled FROM User WHERE username = ?");
        manager.setAuthoritiesByUsernameQuery("SELECT authority FROM Authority WHERE username = ?");
        return manager;
    }
}