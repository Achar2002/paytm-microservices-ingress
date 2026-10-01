package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class EmailServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EmailServiceApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>Email Service</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>📧 Email Service</h1><div class='status'>✅ Service is running on port 9010</div><p>This service handles email notifications, alerts, and communication with users.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>POST /send-email - Send email</li><li>GET /email-status/{emailId} - Get email status</li><li>POST /send-bulk-email - Send bulk emails</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"email-service\",\"port\":9010}";
    }
    
    @PostMapping("/send-email")
    public String sendEmail(@RequestBody String emailData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Email sent successfully\",\"emailId\":\"EMAIL_" + System.currentTimeMillis() + "\"}";
    }
    
    @GetMapping("/email-status/{emailId}")
    public String getEmailStatus(@PathVariable String emailId) {
        return "{\"emailId\":\"" + emailId + "\",\"status\":\"DELIVERED\",\"sentAt\":\"2025-01-01T10:00:00Z\"}";
    }
    
    @PostMapping("/send-bulk-email")
    public String sendBulkEmail(@RequestBody String bulkEmailData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Bulk email sent successfully\",\"batchId\":\"BATCH_" + System.currentTimeMillis() + "\"}";
    }
}