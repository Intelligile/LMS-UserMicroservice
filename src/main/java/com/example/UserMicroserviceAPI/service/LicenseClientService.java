package com.example.UserMicroserviceAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.UserMicroserviceAPI.dto.LicenseRequest;

@Service
public class LicenseClientService {

    @Autowired
    private RestTemplate restTemplate;

    private final String LICENSE_SERVICE_URL = "http://localhost:8081/api/license"; // Use the actual LicenseMicroservice URL or service discovery

    // Method to call LicenseMicroservice to activate a license
    public String activateLicense(LicenseRequest licenseRequest) {
        String url = LICENSE_SERVICE_URL + "/activate";
        return restTemplate.postForObject(url, licenseRequest, String.class);
    }

    // Method to call LicenseMicroservice to check license status
    public boolean checkLicenseStatus(Long licenseId) {
        String url = LICENSE_SERVICE_URL + "/" + licenseId + "/status";
        return restTemplate.getForObject(url, Boolean.class);
    }
}
