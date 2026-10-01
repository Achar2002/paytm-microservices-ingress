package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class BankServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BankServiceApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>Bank Service</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>🏦 Bank Service</h1><div class='status'>✅ Service is running on port 9007</div><p>This service manages bank account linking, verification, and banking operations.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>POST /link-account - Link bank account</li><li>GET /accounts/{userId} - Get linked accounts</li><li>POST /verify-account - Verify bank account</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"bank-service\",\"port\":9007}";
    }
    
    @PostMapping("/link-account")
    public String linkAccount(@RequestBody String accountData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Bank account linked successfully\",\"accountId\":\"ACC_" + System.currentTimeMillis() + "\"}";
    }
    
    @GetMapping("/accounts/{userId}")
    public String getAccounts(@PathVariable String userId) {
        return "{\"userId\":\"" + userId + "\",\"accounts\":[{\"accountId\":\"ACC_001\",\"bankName\":\"HDFC Bank\",\"accountNumber\":\"****1234\",\"type\":\"SAVINGS\"}]}";
    }
    
    @PostMapping("/verify-account")
    public String verifyAccount(@RequestBody String accountData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Account verified successfully\",\"verificationId\":\"VER_" + System.currentTimeMillis() + "\"}";
    }
}