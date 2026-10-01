package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class KycServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(KycServiceApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>KYC Service</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>🔐 KYC Service</h1><div class='status'>✅ Service is running on port 9006</div><p>This service handles Know Your Customer (KYC) verification processes and document validation.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>POST /submit-kyc - Submit KYC documents</li><li>GET /kyc-status/{userId} - Get KYC status</li><li>POST /verify-documents - Verify submitted documents</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"kyc-service\",\"port\":9006}";
    }
    
    @PostMapping("/submit-kyc")
    public String submitKyc(@RequestBody String kycData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"KYC documents submitted successfully\",\"kycId\":\"KYC_" + System.currentTimeMillis() + "\"}";
    }
    
    @GetMapping("/kyc-status/{userId}")
    public String getKycStatus(@PathVariable String userId) {
        return "{\"userId\":\"" + userId + "\",\"status\":\"VERIFIED\",\"verificationDate\":\"2025-01-01\",\"documentsRequired\":false}";
    }
    
    @PostMapping("/verify-documents")
    public String verifyDocuments(@RequestBody String documents) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Documents verified successfully\",\"verificationId\":\"VER_" + System.currentTimeMillis() + "\"}";
    }
}