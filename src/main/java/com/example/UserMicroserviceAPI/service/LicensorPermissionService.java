
package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.controller.PermissionController;

import com.example.UserMicroserviceAPI.model.LicensorAuthority;
import com.example.UserMicroserviceAPI.model.LicensorPermission;
import com.example.UserMicroserviceAPI.model.LicensorPermissionDto;
import com.example.UserMicroserviceAPI.repository.LicensorAuthorityRepository;
import com.example.UserMicroserviceAPI.repository.LicensorPermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class LicensorPermissionService {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private LicensorPermissionRepository permissionRepository;
    @Autowired
    private LicensorAuthorityRepository authorityRepository;

    @Transactional
    public List<LicensorPermissionDto> updatePermissions(List<LicensorPermissionDto> updatedPermissionDtos) {
        List<LicensorPermissionDto> updatedPermissions = new ArrayList<>();

        for (LicensorPermissionDto updatedPermissionDto : updatedPermissionDtos) {
            logger.debug("Attempting to update permission with ID: {}", updatedPermissionDto.getId());

            LicensorPermission permission = permissionRepository.findById(updatedPermissionDto.getId())
                    .orElseThrow(() -> {
                        logger.error("Permission not found with ID: {}", updatedPermissionDto.getId());
                        return new EntityNotFoundException("Permission not found with ID: " + updatedPermissionDto.getId());
                    });

            logger.debug("Found permission: {}", permission);

            permission.setPermission(updatedPermissionDto.getPermission());
            permission.setPermissioinDescription(updatedPermissionDto.getPermissionDescription());

            // Update authorities
            Set<LicensorAuthority> updatedAuthorities = new HashSet<>();
            for (Long authorityId : updatedPermissionDto.getAuthorityIds()) {
                logger.debug("Attempting to find authority with ID: {}", authorityId);

                LicensorAuthority authority = authorityRepository.findById(authorityId)
                        .orElseThrow(() -> {
                            logger.error("Authority not found with ID: {}", authorityId);
                            return new EntityNotFoundException("Authority not found with ID: " + authorityId);
                        });

                logger.debug("Found authority: {}", authority);
                updatedAuthorities.add(authority);
            }

            permission.setAuthorities(updatedAuthorities);

            logger.debug("Saving updated permission: {}", permission);
            LicensorPermission savedPermission = permissionRepository.save(permission);
            LicensorPermissionDto savedDto = toDTO(savedPermission);
            logger.debug("Saved permission DTO: {}", savedDto);
            updatedPermissions.add(savedDto);
        }

        return updatedPermissions;
    }
    public List<LicensorPermissionDto> findAll() {
        List<LicensorPermission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<LicensorPermissionDto> findByRoleName(String roleName) {
        List<LicensorPermission> permissions = permissionRepository.findByAuthorities_Authority(roleName);
        return permissions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private LicensorPermissionDto toDTO(LicensorPermission permission) {
        LicensorPermissionDto dto = new LicensorPermissionDto();
        dto.setId(permission.getId());
        dto.setPermission(permission.getPermission());
        dto.setPermissionDescription(permission.getPermissionDescription());

        Set<Long> authorityIds = permission.getAuthorities().stream()
                .map(LicensorAuthority::getId)
                .collect(Collectors.toSet());
        dto.setAuthorityIds(authorityIds);

        return dto;
    }



    public LicensorPermission save(LicensorPermission permission) {
        return permissionRepository.save(permission);
    }

    public List<LicensorPermission> saveAll(List<LicensorPermission> permissions) {
        return permissionRepository.saveAll(permissions);
    }
}
