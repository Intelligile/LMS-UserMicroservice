package com.example.UserMicroserviceAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.UserMicroserviceAPI.model.UserGroup;
import com.example.UserMicroserviceAPI.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

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

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}