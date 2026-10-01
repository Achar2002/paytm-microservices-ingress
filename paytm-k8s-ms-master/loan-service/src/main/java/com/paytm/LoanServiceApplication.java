package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class LoanServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LoanServiceApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>Loan Service</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>💰 Loan Service</h1><div class='status'>✅ Service is running on port 9008</div><p>This service handles loan applications, processing, and management for personal and business loans.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>POST /apply-loan - Apply for loan</li><li>GET /loan-status/{loanId} - Get loan status</li><li>POST /approve-loan - Approve loan application</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"loan-service\",\"port\":9008}";
    }
    
    @PostMapping("/apply-loan")
    public String applyLoan(@RequestBody String loanData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Loan application submitted successfully\",\"loanId\":\"LOAN_" + System.currentTimeMillis() + "\"}";
    }
    
    @GetMapping("/loan-status/{loanId}")
    public String getLoanStatus(@PathVariable String loanId) {
        return "{\"loanId\":\"" + loanId + "\",\"status\":\"APPROVED\",\"amount\":50000.0,\"interestRate\":12.5,\"tenure\":24}";
    }
    
    @PostMapping("/approve-loan")
    public String approveLoan(@RequestBody String approvalData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Loan approved successfully\",\"approvalId\":\"APP_" + System.currentTimeMillis() + "\"}";
    }
}