package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.model.AuthorizationCode;
import com.example.UserMicroserviceAPI.model.Product;
import com.example.UserMicroserviceAPI.repository.AuthorizationCodeRepository;
import com.example.UserMicroserviceAPI.repository.ProductRepository;
import com.example.UserMicroserviceAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorizationCodeService {

    @Autowired
    private AuthorizationCodeRepository authorizationCodeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository; // Add Product repository for productId validation

    // Validate that the user exists by licenseeId
    private void validateLicensee(Long licenseeId) {
        if (!userRepository.existsById(licenseeId)) {
            throw new IllegalArgumentException("Licensee user ID does not exist.");
        }
    }

    // Method to generate amount-based code with licenseeId
    public AuthorizationCode generateAmountBasedCode(double amount, int periodMonths, double totalCredit, Long licenseeId, Long productId, double discount) {
        validateLicensee(licenseeId); // Validate licenseeId before proceeding
        
        // Check if an AuthorizationCode already exists for this licenseeId
        Optional<AuthorizationCode> existingCodeOpt = authorizationCodeRepository.findByLicenseeId(licenseeId);
        AuthorizationCode authorizationCode;
        if (licenseeId != null) {
            // authorizationCode.setProduct(productRepository.findById(productId).orElse(null));
        } else {
            // Handle the case where productId is null, possibly throw an exception
            throw new IllegalArgumentException("Licensee ID must not be null." + existingCodeOpt.get());
        }
        System.out.println("PRODUCCT IDDDDD"+productId);
        if (existingCodeOpt.isPresent()) {
           
            // Update existing code
            authorizationCode = existingCodeOpt.get();
            authorizationCode.setAmount(amount);
            authorizationCode.setPeriodMonths(periodMonths);
            authorizationCode.setTotalCredit(totalCredit);
            authorizationCode.setDiscount(discount);
            authorizationCode.setLicenseeId(licenseeId);
            authorizationCode.setPeriodEndDate(LocalDate.now().plusMonths(periodMonths)); // Calculate period end date
            authorizationCode.setProduct(productId); // Set the product based on the provided ID
        } else {
            // Create new authorization code
            String code = generateUniqueCode();
            LocalDate periodEndDate = LocalDate.now().plusMonths(periodMonths); // Calculate period end date
            Product product = productRepository.findById(productId).orElse(null); // Set the product based on the provided ID
            authorizationCode = new AuthorizationCode(code, amount, periodMonths, totalCredit, 0, false,licenseeId, periodEndDate , productId,discount);
        }

        return authorizationCodeRepository.save(authorizationCode);
    }

    // Method to generate product-based code with licenseeId
    public AuthorizationCode generateProductBasedCode(int productLimit, Long licenseeId, Long productId, double discount) {
        validateLicensee(licenseeId); // Validate licenseeId before proceeding

        // Check if an AuthorizationCode already exists for this licenseeId
        Optional<AuthorizationCode> existingCodeOpt = authorizationCodeRepository.findByLicenseeId(licenseeId);
        AuthorizationCode authorizationCode;

        if (existingCodeOpt.isPresent()) {
            // Update existing code
            authorizationCode = existingCodeOpt.get();
            authorizationCode.setProductLimit(productLimit);
            authorizationCode.setDiscount(discount);
            authorizationCode.setLicenseeId(licenseeId);
            authorizationCode.setProduct(productId); // Set the product based on the provided ID
        } else {
            // Create new authorization code
            String code = generateUniqueCode();
            authorizationCode = new AuthorizationCode(code, 0, 0, 0, productLimit, false, licenseeId,null, productId,discount); // No amount or totalCredit
        }

        return authorizationCodeRepository.save(authorizationCode);
    }

    // Method to generate combined code with licenseeId
    public AuthorizationCode generateCombinedCode(double amount, int periodMonths, double totalCredit, int productLimit, Long licenseeId, Long productId, double discount) {
        validateLicensee(licenseeId); // Validate licenseeId before proceeding
        
        // Check if an AuthorizationCode already exists for this licenseeId
        Optional<AuthorizationCode> existingCodeOpt = authorizationCodeRepository.findByLicenseeId(licenseeId);
        AuthorizationCode authorizationCode;

        if (existingCodeOpt.isPresent()) {
            // Update existing code
            authorizationCode = existingCodeOpt.get();
            authorizationCode.setAmount(amount);
            authorizationCode.setPeriodMonths(periodMonths);
            authorizationCode.setTotalCredit(totalCredit);
            authorizationCode.setProductLimit(productLimit);
            authorizationCode.setDiscount(discount);
            authorizationCode.setLicenseeId(licenseeId);
            authorizationCode.setPeriodEndDate(LocalDate.now().plusMonths(periodMonths)); // Calculate period end date
            authorizationCode.setProduct(productId); // Set the product based on the provided ID
        } else {
            // Create new authorization code
            String code = generateUniqueCode();
            LocalDate periodEndDate = LocalDate.now().plusMonths(periodMonths); // Calculate period end date
            Product product = productRepository.findById(productId).orElse(null); // Set the product based on the provided ID
            authorizationCode = new AuthorizationCode(code, amount, periodMonths, totalCredit, productLimit, true,licenseeId, periodEndDate , productId,discount);
        }

        return authorizationCodeRepository.save(authorizationCode);
    }

    // Utility to generate a unique authorization code
    private String generateUniqueCode() {
        return UUID.randomUUID().toString(); // Generates a unique code
    }
}
