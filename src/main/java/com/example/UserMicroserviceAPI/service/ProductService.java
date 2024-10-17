package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.model.Product;
import com.example.UserMicroserviceAPI.repository.ProductRepository;
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
public class ProductService {

    private static final String UPLOAD_DIR = "uploads/";  // Directory to store uploaded files

    @Autowired
    private ProductRepository productRepository;

    // Create or Update a product
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Save product with an image
    public Product saveProductWithImage(Product product, MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            product.setImageUrl(imagePath);  // Save the image URL/path
        }
        return productRepository.save(product);
    }

    // Get product by id
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get products by region
    public List<Product> getProductsByRegion(Long regionId) {
        return productRepository.findByRegionId(regionId);
    }

    // Delete product by id
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Insert multiple products
    public List<Product> saveAllProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }

    // Save an image and return the file path
    private String saveImage(MultipartFile imageFile) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);  // Create directory if it doesn't exist
        }
        String fileName = imageFile.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, imageFile.getBytes());
        return filePath.toString();
    }
}
