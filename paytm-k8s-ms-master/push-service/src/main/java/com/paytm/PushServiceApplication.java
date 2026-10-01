package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class PushServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PushServiceApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>Push Service</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>🔔 Push Service</h1><div class='status'>✅ Service is running on port 9012</div><p>This service handles push notifications for mobile apps and web applications.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>POST /send-push - Send push notification</li><li>POST /register-device - Register device for push notifications</li><li>GET /notification-history/{userId} - Get notification history</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"push-service\",\"port\":9012}";
    }
    
    @PostMapping("/send-push")
    public String sendPush(@RequestBody String pushData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Push notification sent successfully\",\"notificationId\":\"PUSH_" + System.currentTimeMillis() + "\"}";
    }
    
    @PostMapping("/register-device")
    public String registerDevice(@RequestBody String deviceData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Device registered successfully\",\"deviceId\":\"DEV_" + System.currentTimeMillis() + "\"}";
    }
    
    @GetMapping("/notification-history/{userId}")
    public String getNotificationHistory(@PathVariable String userId) {
        return "{\"userId\":\"" + userId + "\",\"notifications\":[{\"id\":\"PUSH_001\",\"message\":\"Payment successful\",\"timestamp\":\"2025-01-01T10:00:00Z\"}]}";
    }
}