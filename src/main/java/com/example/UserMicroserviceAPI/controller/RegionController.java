package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.model.Region;
import com.example.UserMicroserviceAPI.service.RegionService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    @Autowired
    private RegionService regionService;

    // Create or update a region
    @PostMapping
    public ResponseEntity<List<Region>> createOrUpdateRegions(@RequestBody List<Region> regions) {
        List<Region> savedRegions = regionService.saveAllRegions(regions);
        return ResponseEntity.ok(savedRegions);
    }

     // Get all regions
     @GetMapping
     public ResponseEntity<List<Region>> getAllRegions() {
         List<Region> savedRegions = regionService.getAllRegions();
         return ResponseEntity.ok(savedRegions);
     }
 


    // Get a region by ID
    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Long id) {
        Optional<Region> region = regionService.getRegionById(id);
        return region.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    



    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegionById(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }

    // Insert multiple products
    @PostMapping("/bulk")
    public ResponseEntity<List<Region>> addMultipleRegions(@RequestBody List<Region> regions) {
        List<Region> savedRegions = regionService.saveAllRegions(regions);
        return ResponseEntity.ok(savedRegions);
    }
}
