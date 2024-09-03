
package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.model.Authority;
import com.example.UserMicroserviceAPI.model.AuthorityDto;
import com.example.UserMicroserviceAPI.model.UserWithRolesDto;
import com.example.UserMicroserviceAPI.service.AuthorityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/authorities")
public class AuthorityController {
    @Autowired
    private AuthorityService authorityService;

    @PostMapping
    public ResponseEntity<String> createRoles(@RequestBody List<AuthorityDto> roles) {
        for (AuthorityDto roleDTO : roles) {
            authorityService.createAuthorityWithPermissions(roleDTO);
        }
        return ResponseEntity.ok(roles.size() + " authorities added successfully");
    }

    @GetMapping
    public ResponseEntity<List<AuthorityDto>> getAllAuthorities() {
        List<AuthorityDto> authorities = authorityService.findAll();
        return ResponseEntity.ok(authorities);
    }

    @GetMapping("/{authorityId}/users")
    public ResponseEntity<List<UserWithRolesDto>> getUsersByAuthority(@PathVariable Long authorityId) {
        try {
            List<UserWithRolesDto> usersWithRoles = authorityService.getUsersWithRolesByAuthorityId(authorityId);
            return ResponseEntity.ok(usersWithRoles);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/{authorityId}")
    public ResponseEntity<AuthorityDto> getAuthorityById(@PathVariable Long authorityId) {
        try {
            AuthorityDto authorityDto = authorityService.getAuthorityById(authorityId);
            return ResponseEntity.ok(authorityDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping("/{authorityId}/permissions")
    public ResponseEntity<AuthorityDto> updateAuthorityPermissions(
            @PathVariable Long authorityId,
            @RequestBody Set<Long> newPermissionIds) {
        try {
            AuthorityDto updatedAuthority = authorityService.updateAuthorityPermissions(authorityId, newPermissionIds);
            return ResponseEntity.ok(updatedAuthority);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

