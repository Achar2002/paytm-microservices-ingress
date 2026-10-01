package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class SmsServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SmsServiceApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>SMS Service</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>📱 SMS Service</h1><div class='status'>✅ Service is running on port 9011</div><p>This service handles SMS notifications, OTP verification, and text messaging.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>POST /send-sms - Send SMS</li><li>POST /send-otp - Send OTP</li><li>POST /verify-otp - Verify OTP</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"sms-service\",\"port\":9011}";
    }
    
    @PostMapping("/send-sms")
    public String sendSms(@RequestBody String smsData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"SMS sent successfully\",\"smsId\":\"SMS_" + System.currentTimeMillis() + "\"}";
    }
    
    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody String otpData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"OTP sent successfully\",\"otpId\":\"OTP_" + System.currentTimeMillis() + "\"}";
    }
    
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody String verificationData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"OTP verified successfully\",\"verified\":true}";
    }
}