package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.model.*;
import com.example.UserMicroserviceAPI.repository.LicensorAuthorityRepository;
import com.example.UserMicroserviceAPI.repository.LicensorPermissionRepository;
import com.example.UserMicroserviceAPI.repository.LicensorUserAuthorityRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LicensorAuthorityService {

    @Autowired
    private LicensorAuthorityRepository authorityRepository;

    @Autowired
    private LicensorPermissionRepository permissionRepository;

    @Autowired
    private LicensorUserAuthorityRepository userAuthorityRepository;

    @Transactional
    public LicensorAuthorityDto updateLicensorAuthorityPermissions(Long authorityId, Set<Long> newPermissionIds) {
        LicensorAuthority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new EntityNotFoundException("LicensorAuthority not found with ID: " + authorityId));

        Set<LicensorPermission> updatedPermissions = new HashSet<>();
        for (Long permissionId : newPermissionIds) {
            LicensorPermission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new EntityNotFoundException("LicensorPermission not found with ID: " + permissionId));
            updatedPermissions.add(permission);
        }

        // Add new permissions that are not already in the authority's permission list
        for (LicensorPermission permission : updatedPermissions) {
            if (!authority.getPermissions().contains(permission)) {
                authority.getPermissions().add(permission);
            }
        }

        // Remove permissions that are no longer in the updated list
        authority.getPermissions().removeIf(permission -> !updatedPermissions.contains(permission));

        LicensorAuthority savedAuthority = authorityRepository.save(authority);
        return toDTO(savedAuthority);
    }

    @Transactional
    public LicensorAuthority createLicensorAuthorityWithPermissions(LicensorAuthorityDto authorityDTO) {
        LicensorAuthority authority = new LicensorAuthority();
        authority.setAuthority(authorityDTO.getAuthority());
        authority.setDescription(authorityDTO.getDescription());

        Set<LicensorPermission> permissions = new HashSet<>();
        for (Long permissionId : authorityDTO.getPermissionIds()) {
            LicensorPermission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new RuntimeException("LicensorPermission not found: " + permissionId));
            permissions.add(permission);
        }
        authority.setPermissions(permissions);
        return authorityRepository.save(authority);
    }

    public List<LicensorUserWithRolesDto> getUsersWithRolesByLicensorAuthorityId(Long authorityId) {
        LicensorAuthority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new EntityNotFoundException("LicensorAuthority not found with ID: " + authorityId));

        List<LicensorUserAuthority> userAuthorities = userAuthorityRepository.findByAuthority(authority);

        return userAuthorities.stream()
                .map(this::convertToLicensorUserWithRolesDto)
                .collect(Collectors.toList());
    }

    private LicensorUserWithRolesDto convertToLicensorUserWithRolesDto(LicensorUserAuthority userAuthority) {
        LicensorUser user = userAuthority.getUser();
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

    public LicensorAuthorityDto getLicensorAuthorityById(Long authorityId) {
        LicensorAuthority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new EntityNotFoundException("LicensorAuthority not found with ID: " + authorityId));

        return toDTO(authority);
    }

    public List<LicensorAuthorityDto> findAllLicensorAuthorities() {
        List<LicensorAuthority> authorities = authorityRepository.findAll();
        return authorities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private LicensorAuthorityDto toDTO(LicensorAuthority authority) {
        LicensorAuthorityDto dto = new LicensorAuthorityDto();
        dto.setId(authority.getId());
        dto.setAuthority(authority.getAuthority());
        dto.setDescription(authority.getDescription());
        dto.setPermissionIds(authority.getPermissions().stream()
                .map(LicensorPermission::getId)
                .collect(Collectors.toSet()));
        return dto;
    }
}
