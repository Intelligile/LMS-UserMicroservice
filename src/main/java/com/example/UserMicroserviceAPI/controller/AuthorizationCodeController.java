package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.dto.AmountBasedRequest;
import com.example.UserMicroserviceAPI.dto.CombinedAuthorizationRequest;
import com.example.UserMicroserviceAPI.dto.ProductBasedRequest;
import com.example.UserMicroserviceAPI.model.AuthorizationCode;
import com.example.UserMicroserviceAPI.service.AuthorizationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/licensor/api/auth-codes")
public class AuthorizationCodeController {

    @Autowired
    private AuthorizationCodeService authorizationCodeService;

    // Endpoint to generate amount-based authorization code with licenseeId
    @PostMapping("/amount-based")
    public ResponseEntity<AuthorizationCode> createAmountBasedCode(@RequestBody AmountBasedRequest request) {
        System.out.println("Incoming request: " + request);
    System.out.println("Product ID IN CONTROLLER: " + request.getProductId());
        AuthorizationCode code = authorizationCodeService.generateAmountBasedCode(
                request.getAmount(), request.getPeriodMonths(), request.getTotalCredit(), request.getLicenseeId(), request.getProductId(), request.getDiscount());
        return ResponseEntity.ok(code);
    }

    // Endpoint to generate product-based authorization code with licenseeId
    @PostMapping("/product-based")
    public ResponseEntity<AuthorizationCode> createProductBasedCode(@RequestBody ProductBasedRequest request) {
        AuthorizationCode code = authorizationCodeService.generateProductBasedCode(request.getProductLimit(), request.getLicenseeId(),  request.getProductId(), request.getDiscount());
        return ResponseEntity.ok(code);
    }

    // Endpoint to generate combined authorization code with licenseeId
    @PostMapping("/combined")
    public ResponseEntity<AuthorizationCode> createCombinedCode(@RequestBody CombinedAuthorizationRequest request) {
        AuthorizationCode code = authorizationCodeService.generateCombinedCode(
                request.getAmount(), request.getPeriodMonths(), request.getTotalCredit(), request.getProductLimit(), request.getLicenseeId(),  request.getProductId(), request.getDiscount());
        return ResponseEntity.ok(code);
    }
}
