package com.example.UserMicroserviceAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.UserMicroserviceAPI.model.LicensorUser;
import com.example.UserMicroserviceAPI.model.LicensorUserGroup;
import com.example.UserMicroserviceAPI.repository.LicensorGroupRepository;
import com.example.UserMicroserviceAPI.repository.LicensorUserGroupRepository;
import com.example.UserMicroserviceAPI.repository.LicensorUserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LicensorGroupService {

    @Autowired
    private LicensorGroupRepository groupRepository;
    @Autowired
    private LicensorUserRepository userRepository;
    @Autowired
    private LicensorUserGroupRepository LicensorUserGroupRepository;
    public List<LicensorUserGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<LicensorUserGroup> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public LicensorUserGroup createGroup(LicensorUserGroup group) {
        return groupRepository.save(group);
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