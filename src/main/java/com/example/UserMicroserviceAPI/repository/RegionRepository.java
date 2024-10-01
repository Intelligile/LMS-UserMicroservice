package com.example.UserMicroserviceAPI.repository;



import com.example.UserMicroserviceAPI.model.Region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    // List<Region> findByRegionId(Long id);
}
