// AuthController.java
package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.dto.LoginRequest;
import com.example.UserMicroserviceAPI.dto.LoginResponse;
import com.example.UserMicroserviceAPI.dto.SignupRequest;
import com.example.UserMicroserviceAPI.service.CustomUserDetailsService;
import com.example.UserMicroserviceAPI.jwt.JwtUtils;
import com.example.UserMicroserviceAPI.model.LicensorUser;
import com.example.UserMicroserviceAPI.service.LicensorUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/licensor/api/auth")
public class LicensorAuthController {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private LicensorUserService userService;
@Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtils jwtUtils;
    private boolean isLicensor = true;
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails;

        try {
            // Load user based on the provided type
            userDetails = customUserDetailsService.loadUserByUsernameBasedOnType(loginRequest.getUsername(), isLicensor);

            // Verify password using PasswordEncoder
            if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            // Perform authentication
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (BadCredentialsException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
        } catch (InternalAuthenticationServiceException e) {
            logger.error("Internal authentication error: {}", e.getMessage());
            return new ResponseEntity<>("Internal error during authentication", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        String roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken, true); // isLicensor = true
        return ResponseEntity.ok(response);
    }






    @PostMapping("/signup")
    public ResponseEntity<?> registerUsers(@Validated @RequestBody List<SignupRequest> signupRequests, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        try {
            List<LicensorUser> registeredUsers = userService.registerUsers(signupRequests);
            return ResponseEntity.ok("Users registered successfully: " + registeredUsers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/users")
    // Assuming only admins should access this
    public ResponseEntity<List<LicensorUser>> getAllUsers() {
        List<LicensorUser> users = userService.getAllUsers();
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
            LicensorUser updatedUser = userService.updateUser(userId, updateUserRequest);
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
    public ResponseEntity<LicensorUser> getUserByUsername(@PathVariable String username) {
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
