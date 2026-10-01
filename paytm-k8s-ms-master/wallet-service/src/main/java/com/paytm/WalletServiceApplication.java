package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class WalletServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WalletServiceApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>Wallet Service</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>💰 Wallet Service</h1><div class='status'>✅ Service is running on port 9002</div><p>This service manages digital wallet operations including balance management, money transfers, and wallet transactions.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>GET /balance/{userId} - Get wallet balance</li><li>POST /add-money - Add money to wallet</li><li>POST /transfer - Transfer money between wallets</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"wallet-service\",\"port\":9002}";
    }
    
    @GetMapping("/balance/{userId}")
    public String getBalance(@PathVariable String userId) {
        return "{\"userId\":\"" + userId + "\",\"balance\":1250.50,\"currency\":\"INR\"}";
    }
    
    @PostMapping("/add-money")
    public String addMoney(@RequestBody String request) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Money added successfully\",\"newBalance\":1350.50}";
    }
    
    @PostMapping("/transfer")
    public String transfer(@RequestBody String request) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Transfer completed\",\"transactionId\":\"TXN_" + System.currentTimeMillis() + "\"}";
    }
}