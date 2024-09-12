// AuthController.java
package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.dto.LoginRequest;
import com.example.UserMicroserviceAPI.dto.LoginResponse;
import com.example.UserMicroserviceAPI.dto.SignupRequest;
import com.example.UserMicroserviceAPI.dto.UserDTO;
import com.example.UserMicroserviceAPI.jwt.JwtUtils;
import com.example.UserMicroserviceAPI.model.User;
import com.example.UserMicroserviceAPI.service.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            exception.printStackTrace();
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        String roles = userDetails.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));
        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUsers(@Validated @RequestBody List<SignupRequest> signupRequests, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        try {
            List<User> registeredUsers = userService.registerUsers(signupRequests);
            return ResponseEntity.ok("Users registered successfully: " + registeredUsers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/users")
    // Assuming only admins should access this
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        System.out.println("LIST OF USERS "+users.toArray());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{userId}")
   // Only admin can update user data
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @Validated @RequestBody SignupRequest updateUserRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        try {
            User updatedUser = userService.updateUser(userId, updateUserRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{userId}")

    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/user/profile/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
            .map(user -> ResponseEntity.ok(user))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // if want to get user profile without authorities and groups 
    // @GetMapping("/user/profile/{username}")
    // public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
    //     return userService.getUserByUsername(username)
    //         .map(user -> {
    //             UserDTO dto = new UserDTO(
    //                 user.getId(),
    //                 user.getUsername(),
    //                 user.getEmail(),
    //                 user.getFirstname(),
    //                 user.getLastname(),
    //                 user.getPhone(),
    //                 user.isEnabled(), null, null
    //             );
    //             return ResponseEntity.ok(dto);
    //         })
    //         .orElseGet(() -> ResponseEntity.notFound().build());
    // }


}
