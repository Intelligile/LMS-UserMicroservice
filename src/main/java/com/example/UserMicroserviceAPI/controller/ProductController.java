package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.model.Product;
import com.example.UserMicroserviceAPI.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Create or update a product
    @PostMapping
    public ResponseEntity<List<Product>> createOrUpdateProducts(@RequestBody List<Product> products) {
        List<Product> savedProducts = productService.saveAllProducts(products);
        return ResponseEntity.ok(savedProducts);
    }

     // Get all products
     @GetMapping
     public ResponseEntity<List<Product>> getAllProducts() {
         List<Product> savedProducts = productService.getAllProducts();
         return ResponseEntity.ok(savedProducts);
     }
 


    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    

    // Get all products by region
    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<Product>> getProductsByRegion(@PathVariable Long regionId) {
        List<Product> products = productService.getProductsByRegion(regionId);
        return ResponseEntity.ok(products);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Insert multiple products
    @PostMapping("/bulk")
    public ResponseEntity<List<Product>> addMultipleProducts(@RequestBody List<Product> products) {
        List<Product> savedProducts = productService.saveAllProducts(products);
        return ResponseEntity.ok(savedProducts);
    }
    // Update a product by ID
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Optional<Product> existingProductOpt = productService.getProductById(id);

        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            // Update fields of the existing product
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setRegionId(updatedProduct.getRegionId());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());  // Optional: If image URL needs to be updated

            // Save the updated product
            Product savedProduct = productService.saveProduct(existingProduct);
            return ResponseEntity.ok(savedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}