package com.example.UserMicroserviceAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.UserMicroserviceAPI.dto.LicensorUserGroupDTO;
import com.example.UserMicroserviceAPI.model.LicensorUser;
import com.example.UserMicroserviceAPI.model.LicensorUserGroup;
import com.example.UserMicroserviceAPI.repository.LicensorGroupRepository;
import com.example.UserMicroserviceAPI.repository.LicensorUserGroupRepository;
import com.example.UserMicroserviceAPI.repository.LicensorUserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LicensorGroupService {

    @Autowired
    private LicensorGroupRepository groupRepository;
    @Autowired
    private LicensorUserRepository userRepository;

    @Autowired
    private LicensorUserService userService;
    @Autowired
    private LicensorUserGroupRepository LicensorUserGroupRepository;
    public List<LicensorUserGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<LicensorUserGroup> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    @Transactional
    public LicensorUserGroup createGroup(LicensorUserGroupDTO groupDTO) {
        // Create and save the group first
        LicensorUserGroup group = new LicensorUserGroup();
        group.setName(groupDTO.getName());
        group.setDescription(groupDTO.getDescription());
    
        // Save the group without users initially
        LicensorUserGroup savedGroup = groupRepository.save(group);
    
        // Fetch user IDs from the DTO
        Set<Long> userIds = groupDTO.getUserGroupIds();
    
        // If there are user IDs, add the users to the group and set the group to the users
        if (userIds != null && !userIds.isEmpty()) {
            // Fetch users by their IDs and assign them to the group
            Set<LicensorUser> users = new HashSet<>();
            for (Long userId : userIds) {
                LicensorUser user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
                users.add(user);
                user.getGroups().add(savedGroup); // Assign group to each user
            }
            
            // Set the users in the group and save the users
            savedGroup.setUsers(users);
            userRepository.saveAll(users); // Save the updated users with the group
        }
    
        // Return the saved group with the users assigned
        return savedGroup;
    }
    

    public LicensorUserGroup updateGroup(Long id, LicensorUserGroup groupDetails) {
        LicensorUserGroup group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
        group.setName(groupDetails.getName());
        group.setDescription(groupDetails.getDescription());
        return groupRepository.save(group);
    }
    @Transactional
    public void revokeUsersFromGroup(Long groupId, List<Long> userIds) {
        LicensorUserGroup group = LicensorUserGroupRepository.findById(groupId)
            .orElseThrow(() -> new EntityNotFoundException("Group not found"));
    
        List<LicensorUser> users = userRepository.findAllById(userIds);
    
        // Remove the association between users and the group
        for (LicensorUser user : users) {
            user.getGroups().remove(group);
        }
        group.getUsers().removeAll(users);
    
        LicensorUserGroupRepository.save(group);
        userRepository.saveAll(users); // Ensure users are updated
        LicensorUserGroupRepository.flush();
    }
    
    
    

    

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}