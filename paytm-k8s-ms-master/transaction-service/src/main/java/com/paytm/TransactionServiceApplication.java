package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class TransactionServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>Transaction Service</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>📊 Transaction Service</h1><div class='status'>✅ Service is running on port 9003</div><p>This service manages transaction history, tracking, and reporting for all financial operations.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>GET /transactions/{userId} - Get user transactions</li><li>POST /record-transaction - Record new transaction</li><li>GET /transaction-report - Generate transaction report</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"transaction-service\",\"port\":9003}";
    }
    
    @GetMapping("/transactions/{userId}")
    public String getTransactions(@PathVariable String userId) {
        return "{\"userId\":\"" + userId + "\",\"transactions\":[{\"id\":\"TXN_001\",\"amount\":100.0,\"type\":\"CREDIT\",\"date\":\"2025-01-01\"}]}";
    }
    
    @PostMapping("/record-transaction")
    public String recordTransaction(@RequestBody String transaction) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Transaction recorded\",\"transactionId\":\"TXN_" + System.currentTimeMillis() + "\"}";
    }
    
    @GetMapping("/transaction-report")
    public String getTransactionReport() {
        return "{\"totalTransactions\":1500,\"totalAmount\":150000.0,\"period\":\"MONTHLY\"}";
    }
}