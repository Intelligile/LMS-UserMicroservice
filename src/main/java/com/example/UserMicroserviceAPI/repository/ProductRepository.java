package com.example.UserMicroserviceAPI.repository;


import com.example.UserMicroserviceAPI.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByRegionId(Long regionId);
}
