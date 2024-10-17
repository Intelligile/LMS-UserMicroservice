
package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.model.LicensorPermission;
import com.example.UserMicroserviceAPI.model.LicensorPermissionDto;
import com.example.UserMicroserviceAPI.service.LicensorPermissionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/licensor/api/permissions")
public class LicensorPermissionController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private LicensorPermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<LicensorPermissionDto>> getAllPermissions() {
        List<LicensorPermissionDto> permissions = permissionService.findAll();
        return ResponseEntity.ok(permissions);
    }
    @GetMapping("/by-role/{roleName}")
    public ResponseEntity<List<LicensorPermissionDto>> getPermissionsByRole(@PathVariable String roleName) {
        List<LicensorPermissionDto> permissions = permissionService.findByRoleName(roleName);
        return ResponseEntity.ok(permissions);
    }

    @PostMapping
    public ResponseEntity<String> createPermissions(@RequestBody List<LicensorPermission> permissions) {
        List<LicensorPermission> savedPermissions = permissionService.saveAll(permissions);
        return ResponseEntity.ok(savedPermissions.size() + " permissions added successfully");
    }

    @PutMapping
    public ResponseEntity<List<LicensorPermissionDto>> updatePermissions(@RequestBody List<LicensorPermissionDto> updatedPermissionDtos) {
        logger.debug("Received request to update {} permissions", updatedPermissionDtos.size());
        try {
            List<LicensorPermissionDto> updatedPermissions = permissionService.updatePermissions(updatedPermissionDtos);
            logger.debug("Successfully updated {} permissions", updatedPermissions.size());
            return ResponseEntity.ok(updatedPermissions);
        } catch (EntityNotFoundException e) {
            logger.error("Entity not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating permissions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
