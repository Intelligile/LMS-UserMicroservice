package com.example.UserMicroserviceAPI.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.UserMicroserviceAPI.model.UserGroup;
import com.example.UserMicroserviceAPI.service.GroupService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/groups")
    public List<UserGroup> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<UserGroup> getGroupById(@PathVariable Long id) {
        Optional<UserGroup> group = groupService.getGroupById(id);
        return group.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/groups/add-group")
    public ResponseEntity<UserGroup> createGroup(@RequestBody UserGroup group) {
        UserGroup createdGroup = groupService.createGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

  

    @PutMapping("/groups/{id}")
    public ResponseEntity<UserGroup> updateGroup(@PathVariable Long id, @RequestBody UserGroup groupDetails) {
        try {
            UserGroup updatedGroup = groupService.updateGroup(id, groupDetails);
            return ResponseEntity.ok(updatedGroup); // Return 200 OK with updated group
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return 500 for other errors
        }
    }
    
    
    

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        System.out.print("DELETE ID=>"+id);
        return ResponseEntity.noContent().build();
    }
}
