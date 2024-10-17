package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.model.Product;
import com.example.UserMicroserviceAPI.model.Region;
import com.example.UserMicroserviceAPI.repository.ProductRepository;
import com.example.UserMicroserviceAPI.repository.RegionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

   

    @Autowired
    private RegionRepository regionRepository;

    // Create or Update a region
    public Region saveRegion(Region region) {
        return regionRepository.save(region);
    }


    // Get region by id
    public Optional<Region> getRegionById(Long id) {
        return regionRepository.findById(id);
    }


    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

   

    // Delete region by id
    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }

    // Insert multiple regions
    public List<Region> saveAllRegions(List<Region> regions) {
        return regionRepository.saveAll(regions);
    }

 
}
