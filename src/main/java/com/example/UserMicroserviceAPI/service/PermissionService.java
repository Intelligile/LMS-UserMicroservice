
package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.controller.PermissionController;
import com.example.UserMicroserviceAPI.model.Authority;
import com.example.UserMicroserviceAPI.model.Permission;
import com.example.UserMicroserviceAPI.model.PermissionDto;
import com.example.UserMicroserviceAPI.repository.AuthorityRepository;
import com.example.UserMicroserviceAPI.repository.PermissionRepository;
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
public class PermissionService {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Transactional
    public List<PermissionDto> updatePermissions(List<PermissionDto> updatedPermissionDtos) {
        List<PermissionDto> updatedPermissions = new ArrayList<>();

        for (PermissionDto updatedPermissionDto : updatedPermissionDtos) {
            logger.debug("Attempting to update permission with ID: {}", updatedPermissionDto.getId());

            Permission permission = permissionRepository.findById(updatedPermissionDto.getId())
                    .orElseThrow(() -> {
                        logger.error("Permission not found with ID: {}", updatedPermissionDto.getId());
                        return new EntityNotFoundException("Permission not found with ID: " + updatedPermissionDto.getId());
                    });

            logger.debug("Found permission: {}", permission);

            permission.setPermission(updatedPermissionDto.getPermission());
            permission.setPermissioinDescription(updatedPermissionDto.getPermissionDescription());

            // Update authorities
            Set<Authority> updatedAuthorities = new HashSet<>();
            for (Long authorityId : updatedPermissionDto.getAuthorityIds()) {
                logger.debug("Attempting to find authority with ID: {}", authorityId);

                Authority authority = authorityRepository.findById(authorityId)
                        .orElseThrow(() -> {
                            logger.error("Authority not found with ID: {}", authorityId);
                            return new EntityNotFoundException("Authority not found with ID: " + authorityId);
                        });

                logger.debug("Found authority: {}", authority);
                updatedAuthorities.add(authority);
            }

            permission.setAuthorities(updatedAuthorities);

            logger.debug("Saving updated permission: {}", permission);
            Permission savedPermission = permissionRepository.save(permission);
            PermissionDto savedDto = toDTO(savedPermission);
            logger.debug("Saved permission DTO: {}", savedDto);
            updatedPermissions.add(savedDto);
        }

        return updatedPermissions;
    }
    public List<PermissionDto> findAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<PermissionDto> findByRoleName(String roleName) {
        List<Permission> permissions = permissionRepository.findByAuthorities_Authority(roleName);
        return permissions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PermissionDto toDTO(Permission permission) {
        PermissionDto dto = new PermissionDto();
        dto.setId(permission.getId());
        dto.setPermission(permission.getPermission());
        dto.setPermissionDescription(permission.getPermissionDescription());

        Set<Long> authorityIds = permission.getAuthorities().stream()
                .map(Authority::getId)
                .collect(Collectors.toSet());
        dto.setAuthorityIds(authorityIds);

        return dto;
    }



    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    public List<Permission> saveAll(List<Permission> permissions) {
        return permissionRepository.saveAll(permissions);
    }
}
