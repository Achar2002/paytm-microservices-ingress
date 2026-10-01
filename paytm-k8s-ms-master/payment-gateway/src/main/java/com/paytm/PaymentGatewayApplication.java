package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class PaymentGatewayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PaymentGatewayApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>Payment Gateway</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>🏦 Payment Gateway Service</h1><div class='status'>✅ Service is running on port 9001</div><p>This micrro-service handles secure payment processing for all transactions including credit cards, debit cards, net banking, and UPI payments.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>POST /process-payment - Process payment</li><li>GET /payment-status/{id} - Get payment status</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"payment-gateway\",\"port\":9001}";
    }
    
    @PostMapping("/process-payment")
    public String processPayment(@RequestBody String paymentData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Payment processed successfully\",\"transactionId\":\"TXN_" + System.currentTimeMillis() + "\"}";
    }
    
    @GetMapping("/payment-status/{id}")
    public String getPaymentStatus(@PathVariable String id) {
        return "{\"transactionId\":\"" + id + "\",\"status\":\"COMPLETED\",\"amount\":100.00}";
    }
}
