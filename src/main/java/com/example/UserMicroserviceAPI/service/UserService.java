package com.example.UserMicroserviceAPI.service;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.UserMicroserviceAPI.model.UserWithRolesDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.example.UserMicroserviceAPI.dto.SignupRequest;
import com.example.UserMicroserviceAPI.model.Authority;

import com.example.UserMicroserviceAPI.model.User;
import com.example.UserMicroserviceAPI.model.UserAuthority;
import com.example.UserMicroserviceAPI.model.UserGroup;
import com.example.UserMicroserviceAPI.repository.AuthorityRepository;
import com.example.UserMicroserviceAPI.repository.UserAuthorityRepository;
import com.example.UserMicroserviceAPI.repository.UserGroupRepository;
import com.example.UserMicroserviceAPI.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

      @Autowired
    private UserGroupRepository userGroupRepository;

    @Transactional
    public void assignUsersToGroup(List<Long> userIds, Long groupId) {
        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<User> users = userRepository.findAllById(userIds);
        for (User user : users) {
            user.getGroups().add(group);
        }
        userRepository.saveAll(users);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Assumes UserRepository extends JpaRepository<User, Long>
    }

    public User updateUser(Long userId, SignupRequest updateUserRequest) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        user.setUsername(updateUserRequest.getUsername());
        user.setPassword(updateUserRequest.getPassword());
        user.setEmail(updateUserRequest.getEmail());
        user.setFirstname(updateUserRequest.getFirstname());
        user.setLastname(updateUserRequest.getLastname());
        user.setPhone(updateUserRequest.getPhone());

    

        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(userId);
    }
   
    public List<UserWithRolesDto> getAllUsersWithRoles() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserWithRolesDto)
                .collect(Collectors.toList());
    }

    private UserWithRolesDto convertToUserWithRolesDto(User user) {
        List<String> roles = user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toList());

        return new UserWithRolesDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                roles
        );
    }

  
    
       @Transactional
public List<User> registerUsers(List<SignupRequest> signupRequests) {
    List<User> registeredUsers = new ArrayList<>();
    List<String> errors = new ArrayList<>();
    
   

    // Constructor injection or @Autowired could be used here to initialize these dependencies
    
    for (SignupRequest signupRequest : signupRequests) {
        try {
            logger.debug("Checking username: {}", signupRequest.getUsername());
            logger.debug("Checking email: {}", signupRequest.getEmail());

            // Check if user already exists
            if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
                errors.add("Username already exists: " + signupRequest.getUsername());
                continue; // Skip to the next user
            }
            if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
                errors.add("Email already exists: " + signupRequest.getEmail());
                continue; // Skip to the next user
            }

            User user = new User();
            user.setUsername(signupRequest.getUsername());
            user.setFirstname(signupRequest.getFirstname());
            user.setLastname(signupRequest.getLastname());
            user.setPhone(signupRequest.getPhone());
            user.setEmail(signupRequest.getEmail());
            user.setEnabled(signupRequest.getEnabled());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

            // Save the user
            User savedUser = userRepository.save(user);

            // Save authorities and user_authorities relationships
            // Handle authorities
            if (signupRequest.getAuthorityIDs() != null && !signupRequest.getAuthorityIDs().isEmpty()) {
                Set<Authority> authorities = new HashSet<>();
                for (Long authorityId : signupRequest.getAuthorityIDs()) {
                    try {
                        Authority authority = authorityRepository.findById(authorityId)
                            .orElseThrow(() -> new AuthException("Authority not found"));

                        // Save the relationship in user_authorities table
                        UserAuthority userAuthority = new UserAuthority();
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
            } else {
                logger.info("No authorities provided for user: {}", signupRequest.getUsername());
            }


            // Add saved user to the list
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
    public User updateUserRoles(Long userId, Set<Long> newAuthorityIds) {
        logger.debug("Updating roles for user with ID: {}", userId);

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            logger.debug("Found user: {}", user);

            // Fetch existing user authorities
            List<UserAuthority> existingAuthorities = userAuthorityRepository.findByUserId(userId);

            // Remove authorities that are not in the new set
            for (UserAuthority ua : existingAuthorities) {
                if (!newAuthorityIds.contains(ua.getAuthority().getId())) {
                    userAuthorityRepository.delete(ua);
                    logger.debug("Deleted user authority: {}", ua);
                }
            }

            // Add new authorities
            Set<Authority> newAuthorities = new HashSet<>();
            for (Long authorityId : newAuthorityIds) {
                Authority authority = authorityRepository.findById(authorityId)
                        .orElseThrow(() -> new EntityNotFoundException("Authority not found with ID: " + authorityId));

                if (existingAuthorities.stream().noneMatch(ua -> ua.getAuthority().getId().equals(authorityId))) {
                    UserAuthority userAuthority = new UserAuthority(user, authority);
                    userAuthorityRepository.save(userAuthority);
                    logger.debug("Saved new user authority: {}", userAuthority);
                }

                newAuthorities.add(authority);
            }

            user.setAuthorities(newAuthorities);
            User savedUser = userRepository.save(user);
            logger.debug("Saved updated user: {}", savedUser);

            return savedUser;
        } catch (OptimisticLockingFailureException e) {
            logger.error("Concurrent modification detected", e);
            throw new ConcurrentModificationException("The user roles were modified by another transaction. Please try again.");
        } catch (Exception e) {
            logger.error("Error in updateUserRoles", e);
            throw e;
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
