package com.example.UserMicroserviceAPI.service;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.UserMicroserviceAPI.dto.LicenseRequest;
import com.example.UserMicroserviceAPI.dto.SignupRequest;
import com.example.UserMicroserviceAPI.jwt.AuthEntryPointJwt;
import com.example.UserMicroserviceAPI.model.LicensorAuthority;
import com.example.UserMicroserviceAPI.model.LicensorUser;
import com.example.UserMicroserviceAPI.model.LicensorUserAuthority;
import com.example.UserMicroserviceAPI.model.LicensorUserGroup;
import com.example.UserMicroserviceAPI.model.LicensorUserWithRolesDto;

import com.example.UserMicroserviceAPI.repository.LicensorAuthorityRepository;
import com.example.UserMicroserviceAPI.repository.LicensorUserAuthorityRepository;
import com.example.UserMicroserviceAPI.repository.LicensorUserGroupRepository;
import com.example.UserMicroserviceAPI.repository.LicensorUserRepository;

import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class LicensorUserService {

    @Autowired
    private LicensorUserRepository userRepository;
    
    @Autowired
    private LicensorAuthorityRepository authorityRepository;

    @Autowired
    private LicensorUserAuthorityRepository userAuthorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LicenseClientService licenseClientService;
    private static final Logger logger = LoggerFactory.getLogger(LicensorUserService.class);

    @Autowired
    private LicensorUserGroupRepository userGroupRepository;

    
     @Autowired
    private AuthEntryPointJwt authEntryPointJwt; // Inject AuthEntryPointJwt

     // Update the method to include HttpServletRequest
    public String activateUserLicense(LicenseRequest request, HttpServletRequest httpRequest, HttpServletResponse response) {
        try {
            // Fetch the user's activation limit
            int userActivationLimit = getUserActivationLimit(request.getUserId());

            // Check how many features are included in the request
            int featuresToActivate = request.getFeatures().size();

            // Validate that the number of features does not exceed the user's activation limit
            if (featuresToActivate > userActivationLimit) {
                throw new IllegalArgumentException("The number of features to activate exceeds your activation limit.");
            }

            return licenseClientService.activateLicense(request);
        }  catch (IllegalArgumentException ex) {
            // Handle the exception using AuthEntryPointJwt
            try {
                authEntryPointJwt.handleIllegalArgumentException(response, ex.getMessage(), httpRequest.getServletPath());
            } catch (java.io.IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null; // Optionally return null or throw a custom exception
        }
    }

        // Method to fetch the user's activation limit from the database
        // Method to fetch the user's activation limit from the database
        private int getUserActivationLimit(Long userId) {
            // Log the userId for debugging
            System.out.println("Fetching activation limit for userId: " + userId);
            
            Optional<LicensorUser> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                return userOptional.get().getActivationLimit();
            } else {
                throw new IllegalArgumentException("User not found.");
            }
        }
        
    // Method to check if the user's license is valid
    public boolean isLicenseValid(Long licenseId) {
        return licenseClientService.checkLicenseStatus(licenseId);
    }

    @Transactional
    public void assignUsersToGroup(List<Long> userIds, Long groupId) {
        LicensorUserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<LicensorUser> users = userRepository.findAllById(userIds).stream()
                .map(user -> (LicensorUser) user)
                .collect(Collectors.toList());

        for (LicensorUser user : users) {
            user.getGroups().add(group);
        }
        userRepository.saveAll(users);
    }

    public List<LicensorUser> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> (LicensorUser) user)
                .collect(Collectors.toList());
    }

    public LicensorUser updateUser(Long userId, SignupRequest updateUserRequest) {
        Optional<LicensorUser> userOptional = userRepository.findById(userId)
                .map(user -> (LicensorUser) user);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        LicensorUser user = userOptional.get();
        user.setUsername(updateUserRequest.getUsername());
        user.setPassword(updateUserRequest.getPassword());
        user.setEmail(updateUserRequest.getEmail());
        user.setFirstname(updateUserRequest.getFirstname());
        user.setLastname(updateUserRequest.getLastname());
        user.setPhone(updateUserRequest.getPhone());

        return (LicensorUser) userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        Optional<LicensorUser> userOptional = userRepository.findById(userId)
                .map(user -> (LicensorUser) user);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(userId);
    }

    public List<LicensorUserWithRolesDto> getAllUsersWithRoles() {
        List<LicensorUser> users = userRepository.findAll().stream()
                .map(user -> (LicensorUser) user)
                .collect(Collectors.toList());

        return users.stream()
                .map(this::convertToLicensorUserWithRolesDto)
                .collect(Collectors.toList());
    }

    private LicensorUserWithRolesDto convertToLicensorUserWithRolesDto(LicensorUser user) {
        List<String> roles = user.getAuthorities().stream()
                .map(LicensorAuthority::getAuthority)
                .collect(Collectors.toList());

        return new LicensorUserWithRolesDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                roles
        );
    }

    @Transactional
    public List<LicensorUser> registerUsers(List<SignupRequest> signupRequests) {
        List<LicensorUser> registeredUsers = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (SignupRequest signupRequest : signupRequests) {
            try {
                logger.debug("Checking username: {}", signupRequest.getUsername());
                logger.debug("Checking email: {}", signupRequest.getEmail());

                if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
                    errors.add("Username already exists: " + signupRequest.getUsername());
                    continue;
                }
                if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
                    errors.add("Email already exists: " + signupRequest.getEmail());
                    continue;
                }

                LicensorUser user = new LicensorUser();
                user.setUsername(signupRequest.getUsername());
                user.setFirstname(signupRequest.getFirstname());
                user.setLastname(signupRequest.getLastname());
                user.setPhone(signupRequest.getPhone());
                user.setEmail(signupRequest.getEmail());
                user.setEnabled(signupRequest.getEnabled());
                user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
                user.setActivationLimit(signupRequest.getActivationLimit());
                user.setTerittory(signupRequest.getTerittory());

                LicensorUser savedUser = (LicensorUser) userRepository.save(user);

                if (signupRequest.getAuthorityIDs() != null && !signupRequest.getAuthorityIDs().isEmpty()) {
                    Set<LicensorAuthority> authorities = new HashSet<>();
                    for (Long authorityId : signupRequest.getAuthorityIDs()) {
                        try {
                            LicensorAuthority authority = authorityRepository.findById(authorityId)
                                .orElseThrow(() -> new AuthException("Authority not found"));

                                LicensorUserAuthority userAuthority = new LicensorUserAuthority();
                            userAuthority.setUser(savedUser);
                            userAuthority.setAuthority(authority);
                            userAuthorityRepository.save(userAuthority);

                            authorities.add(authority);
                        } catch (AuthException e) {
                            logger.error("Authority ID {} not found: {}", authorityId, e.getMessage());
                            errors.add("Authority ID " + authorityId + " not found.");
                        }
                    }
                    savedUser.setAuthorities(authorities);
                }

                registeredUsers.add(savedUser);

            } catch (Exception e) {
                logger.error("Error registering user {}: {}", signupRequest.getUsername(), e.getMessage());
                errors.add("Error registering user " + signupRequest.getUsername() + ": " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            String errorMessage = "Errors occurred during registration: " + String.join(", ", errors);
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }

        return registeredUsers;
    }

    @Transactional
    public LicensorUser updateUserRoles(Long userId, Set<Long> newAuthorityIds) {
        logger.debug("Updating roles for user with ID: {}", userId);

        try {
            LicensorUser user = (LicensorUser) userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            logger.debug("Found user: {}", user);

            List<LicensorUserAuthority> existingAuthorities = userAuthorityRepository.findByUserId(userId);

            for (LicensorUserAuthority ua : existingAuthorities) {
                if (!newAuthorityIds.contains(ua.getAuthority().getId())) {
                    userAuthorityRepository.delete(ua);
                    logger.debug("Deleted user authority: {}", ua);
                }
            }

            Set<LicensorAuthority> newAuthorities = new HashSet<>();
            for (Long authorityId : newAuthorityIds) {
                LicensorAuthority authority = authorityRepository.findById(authorityId)
                        .orElseThrow(() -> new EntityNotFoundException("Authority not found with ID: " + authorityId));

                if (existingAuthorities.stream().noneMatch(ua -> ua.getAuthority().getId().equals(authorityId))) {
                    LicensorUserAuthority userAuthority = new LicensorUserAuthority(user, authority);
                    userAuthorityRepository.save(userAuthority);
                    logger.debug("Saved new user authority: {}", userAuthority);
                }

                newAuthorities.add(authority);
            }

            user.setAuthorities(newAuthorities);
            return (LicensorUser) userRepository.save(user);

        } catch (OptimisticLockingFailureException e) {
            logger.error("Concurrent modification detected", e);
            throw new ConcurrentModificationException("The user roles were modified by another transaction. Please try again.");
        } catch (Exception e) {
            logger.error("Error in updateUserRoles", e);
            throw e;
        }
    }

    public Optional<LicensorUser> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(user -> (LicensorUser) user);
    }


}
