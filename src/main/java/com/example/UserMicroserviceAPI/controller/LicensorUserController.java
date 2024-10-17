
// UserController.java
package com.example.UserMicroserviceAPI.controller;
import com.example.UserMicroserviceAPI.dto.LicenseRequest;
import com.example.UserMicroserviceAPI.model.LicensorUser;
import com.example.UserMicroserviceAPI.model.LicensorUserWithRolesDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.UserMicroserviceAPI.service.LicensorUserService;
import com.example.UserMicroserviceAPI.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Set;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/licensor/api/users")
public class LicensorUserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private LicensorUserService userService;
    @Autowired
    private UserService licenseeUserService;

    // @GetMapping("/hello")
    // public String sayHi() {
    //     return "hello";
    // }

    // @PreAuthorize("hasRole('USER')")
    // @GetMapping("/user")
    // public String userEndPoint() {
    //     return "hello user";
    // }

    // @PreAuthorize("hasRole('ADMIN')")
    // @GetMapping("/admin")
    // public String adminEndPoint() {
    //     return "hello admin";
    // }


    @GetMapping("/with-roles")
    public ResponseEntity<List<LicensorUserWithRolesDto>> getAllUsersWithRoles() {
        try {
            List<LicensorUserWithRolesDto> usersWithRoles = userService.getAllUsersWithRoles();
            return ResponseEntity.ok(usersWithRoles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}/roles")
    public ResponseEntity<?> updateUserRoles(@PathVariable Long userId, @RequestBody Set<Long> newAuthorityIds) {
        try {
            LicensorUser updatedUser = userService.updateUserRoles(userId, newAuthorityIds);
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException e) {
            logger.error("Error updating user roles: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ConcurrentModificationException e) {
            logger.error("Concurrent modification error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error updating user roles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

        @PostMapping("/activate-license")
    public ResponseEntity<String> activateLicense(@RequestBody LicenseRequest request, HttpServletRequest httpRequest,HttpServletResponse httpResponse) {
       // Validate that the licensee user exists
       if (!licenseeUserService.userExists(request.getLicenseeUserId())) {
   
        return ResponseEntity.badRequest().body("Licensee user ID not found.");
    }

    // Proceed to activate the license
    try {
        String result = userService.activateUserLicense(request,httpRequest,httpResponse);
        return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }

    @GetMapping("/license/{licenseId}/valid")
    public ResponseEntity<Boolean> checkLicenseValidity(@PathVariable Long licenseId) {
        boolean isValid = userService.isLicenseValid(licenseId);
        return ResponseEntity.ok(isValid);
    }
}
