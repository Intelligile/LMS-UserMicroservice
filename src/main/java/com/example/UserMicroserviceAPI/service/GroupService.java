package com.example.UserMicroserviceAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.UserMicroserviceAPI.model.User;
import com.example.UserMicroserviceAPI.model.UserGroup;
import com.example.UserMicroserviceAPI.repository.GroupRepository;
import com.example.UserMicroserviceAPI.repository.UserGroupRepository;
import com.example.UserMicroserviceAPI.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    public List<UserGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<UserGroup> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public UserGroup createGroup(UserGroup group) {
        return groupRepository.save(group);
    }
    

    public UserGroup updateGroup(Long id, UserGroup groupDetails) {
        UserGroup group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
        group.setName(groupDetails.getName());
        group.setDescription(groupDetails.getDescription());
        return groupRepository.save(group);
    }
    @Transactional
    public void revokeUsersFromGroup(Long groupId, List<Long> userIds) {
        UserGroup group = userGroupRepository.findById(groupId)
            .orElseThrow(() -> new EntityNotFoundException("Group not found"));
    
        List<User> users = userRepository.findAllById(userIds);
    
        // Remove the association between users and the group
        for (User user : users) {
            user.getGroups().remove(group);
        }
        group.getUsers().removeAll(users);
    
        userGroupRepository.save(group);
        userRepository.saveAll(users); // Ensure users are updated
        userGroupRepository.flush();
    }
    
    
    

    

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}