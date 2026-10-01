package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class InvestmentServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(InvestmentServiceApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>Investment Service</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>📈 Investment Service</h1><div class='status'>✅ Service is running on port 9009</div><p>This service manages investment portfolios, mutual funds, and financial planning.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>GET /portfolio/{userId} - Get investment portfolio</li><li>POST /invest - Make investment</li><li>GET /market-data - Get market data</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"investment-service\",\"port\":9009}";
    }
    
    @GetMapping("/portfolio/{userId}")
    public String getPortfolio(@PathVariable String userId) {
        return "{\"userId\":\"" + userId + "\",\"totalValue\":25000.0,\"investments\":[{\"name\":\"SBI Mutual Fund\",\"value\":15000.0,\"returns\":5.2}]}";
    }
    
    @PostMapping("/invest")
    public String invest(@RequestBody String investmentData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Investment completed successfully\",\"investmentId\":\"INV_" + System.currentTimeMillis() + "\"}";
    }
    
    @GetMapping("/market-data")
    public String getMarketData() {
        return "{\"nifty\":18500.0,\"sensex\":62000.0,\"gold\":55000.0,\"silver\":70000.0}";
    }
}