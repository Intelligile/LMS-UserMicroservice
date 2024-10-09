
package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.model.Permission;
import com.example.UserMicroserviceAPI.model.PermissionDto;
import com.example.UserMicroserviceAPI.service.PermissionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        List<PermissionDto> permissions = permissionService.findAll();
        return ResponseEntity.ok(permissions);
    }
    @GetMapping("/by-role/{roleName}")
    public ResponseEntity<List<PermissionDto>> getPermissionsByRole(@PathVariable String roleName) {
        List<PermissionDto> permissions = permissionService.findByRoleName(roleName);
        return ResponseEntity.ok(permissions);
    }

    @PostMapping
    public ResponseEntity<String> createPermissions(@RequestBody List<Permission> permissions) {
        List<Permission> savedPermissions = permissionService.saveAll(permissions);
        return ResponseEntity.ok(savedPermissions.size() + " permissions added successfully");
    }

    @PutMapping
    public ResponseEntity<List<PermissionDto>> updatePermissions(@RequestBody List<PermissionDto> updatedPermissionDtos) {
        logger.debug("Received request to update {} permissions", updatedPermissionDtos.size());
        try {
            List<PermissionDto> updatedPermissions = permissionService.updatePermissions(updatedPermissionDtos);
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
