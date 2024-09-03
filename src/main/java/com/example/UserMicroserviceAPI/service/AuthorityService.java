
package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.model.*;
import com.example.UserMicroserviceAPI.repository.AuthorityRepository;
import com.example.UserMicroserviceAPI.repository.PermissionRepository;

import com.example.UserMicroserviceAPI.repository.UserAuthorityRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Transactional
    public AuthorityDto updateAuthorityPermissions(Long authorityId, Set<Long> newPermissionIds) {
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new EntityNotFoundException("Authority not found with ID: " + authorityId));

        Set<Permission> updatedPermissions = new HashSet<>();
        for (Long permissionId : newPermissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new EntityNotFoundException("Permission not found with ID: " + permissionId));
            updatedPermissions.add(permission);
        }

        // Add new permissions that are not already in the authority's permission list
        for (Permission permission : updatedPermissions) {
            if (!authority.getPermissions().contains(permission)) {
                authority.getPermissions().add(permission);
            }
        }

        // Remove permissions that are no longer in the updated list
        authority.getPermissions().removeIf(permission -> !updatedPermissions.contains(permission));

        Authority savedAuthority = authorityRepository.save(authority);
        return toDTO(savedAuthority);
    }
    @Transactional
    public Authority createAuthorityWithPermissions(AuthorityDto authorityDTO) {
        Authority authority = new Authority();
        authority.setAuthority(authorityDTO.getAuthority());
        authority.setAuthorityDescription(authorityDTO.getDescription());

        Set<Permission> permissions = new HashSet<>();
        for (Long permissionId : authorityDTO.getPermissionIds()) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));
            permissions.add(permission);
        }
        authority.setPermissions(permissions);
        return authorityRepository.save(authority);
    }

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    public List<UserWithRolesDto> getUsersWithRolesByAuthorityId(Long authorityId) {
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new EntityNotFoundException("Authority not found with ID: " + authorityId));

        List<UserAuthority> userAuthorities = userAuthorityRepository.findByAuthority(authority);

        return userAuthorities.stream()
                .map(this::convertToUserWithRolesDto)
                .collect(Collectors.toList());
    }
    private UserWithRolesDto convertToUserWithRolesDto(UserAuthority userAuthority) {
        User user = userAuthority.getUser();
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


    public AuthorityDto getAuthorityById(Long authorityId) {
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new EntityNotFoundException("Authority not found with ID: " + authorityId));

        return toDTO(authority);
    }

    public List<AuthorityDto> findAll() {
        List<Authority> authorities = authorityRepository.findAll();
        return authorities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private AuthorityDto toDTO(Authority authority) {
        AuthorityDto dto = new AuthorityDto();
        dto.setId(authority.getId());
        dto.setAuthority(authority.getAuthority());
        dto.setDescription(authority.getAuthorityDescription());
        dto.setPermissionIds(authority.getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toSet()));
        dto.setUserIds(authority.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toSet()));  // Adding user IDs to the DTO
        return dto;
    }
}
