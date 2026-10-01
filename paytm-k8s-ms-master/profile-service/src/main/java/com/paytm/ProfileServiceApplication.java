package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class ProfileServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>Profile Service</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>📋 Profile Service</h1><div class='status'>✅ Service is running on port 9005</div><p>This service manages user profiles, personal information, and account settings.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>GET /profile/{userId} - Get user profile</li><li>PUT /profile/{userId} - Update user profile</li><li>POST /upload-photo - Upload profile photo</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"profile-service\",\"port\":9005}";
    }
    
    @GetMapping("/profile/{userId}")
    public String getProfile(@PathVariable String userId) {
        return "{\"userId\":\"" + userId + "\",\"name\":\"John Doe\",\"email\":\"john@example.com\",\"phone\":\"+91-9876543210\",\"address\":\"123 Main St, Mumbai\"}";
    }
    
    @PutMapping("/profile/{userId}")
    public String updateProfile(@PathVariable String userId, @RequestBody String profileData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Profile updated successfully\",\"userId\":\"" + userId + "\"}";
    }
    
    @PostMapping("/upload-photo")
    public String uploadPhoto(@RequestBody String photoData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Photo uploaded successfully\",\"photoUrl\":\"https://example.com/photo.jpg\"}";
    }
}