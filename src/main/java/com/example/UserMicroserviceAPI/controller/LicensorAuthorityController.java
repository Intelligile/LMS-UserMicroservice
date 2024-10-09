
package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.model.LicensorAuthorityDto;
import com.example.UserMicroserviceAPI.model.LicensorUserWithRolesDto;
import com.example.UserMicroserviceAPI.service.LicensorAuthorityService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/licensor/api/authorities")
public class LicensorAuthorityController {
    @Autowired
    private  LicensorAuthorityService authorityService;

    @PostMapping
    public ResponseEntity<String> createRoles(@RequestBody List< LicensorAuthorityDto> roles) {
        for (LicensorAuthorityDto roleDTO : roles) {
            authorityService.createLicensorAuthorityWithPermissions(roleDTO);
        }
        return ResponseEntity.ok(roles.size() + " authorities added successfully");
    }

    @GetMapping
    public ResponseEntity<List<LicensorAuthorityDto>> getAllAuthorities() {
        List<LicensorAuthorityDto> authorities = authorityService.findAllLicensorAuthorities();
        return ResponseEntity.ok(authorities);
    }

    @GetMapping("/{authorityId}/users")
    public ResponseEntity<List<LicensorUserWithRolesDto>> getUsersByAuthority(@PathVariable Long authorityId) {
        try {
            List<LicensorUserWithRolesDto> usersWithRoles = authorityService.getUsersWithRolesByLicensorAuthorityId(authorityId);
            return ResponseEntity.ok(usersWithRoles);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/{authorityId}")
    public ResponseEntity<LicensorAuthorityDto> getAuthorityById(@PathVariable Long authorityId) {
        try {
            LicensorAuthorityDto authorityDto = authorityService.getLicensorAuthorityById(authorityId);
            return ResponseEntity.ok(authorityDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping("/{authorityId}/permissions")
    public ResponseEntity<LicensorAuthorityDto> updateAuthorityPermissions(
            @PathVariable Long authorityId,
            @RequestBody Set<Long> newPermissionIds) {
        try {
            LicensorAuthorityDto updatedAuthority = authorityService.updateLicensorAuthorityPermissions(authorityId, newPermissionIds);
            return ResponseEntity.ok(updatedAuthority);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

