package com.paytm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class UserServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
    
    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><head><title>User Service</title><style>body{font-family:Arial;background:#f4f4f4;margin:0;padding:20px}.container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:10px;box-shadow:0 0 20px rgba(0,0,0,0.1)}h1{color:#333;text-align:center;margin-bottom:30px}.status{background:#e8f5e8;border-left:4px solid #4caf50;padding:15px;margin:20px 0;border-radius:5px}</style></head><body><div class='container'><h1>👤 User Service</h1><div class='status'>✅ Service is running on port 9004</div><p>This service handles user registration, authentication, and basic user management operations.</p><h3>Available Endpoints:</h3><ul><li>GET /health - Health check</li><li>POST /register - Register new user</li><li>POST /login - User login</li><li>GET /user/{id} - Get user details</li></ul></div></body></html>";
    }
    
    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"service\":\"user-service\",\"port\":9004}";
    }
    
    @PostMapping("/register")
    public String register(@RequestBody String userData) {
        return "{\"status\":\"SUCCESS\",\"message\":\"User registered successfully\",\"userId\":\"USR_" + System.currentTimeMillis() + "\"}";
    }
    
    @PostMapping("/login")
    public String login(@RequestBody String credentials) {
        return "{\"status\":\"SUCCESS\",\"message\":\"Login successful\",\"token\":\"JWT_TOKEN_" + System.currentTimeMillis() + "\"}";
    }
    
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable String id) {
        return "{\"userId\":\"" + id + "\",\"name\":\"John Doe\",\"email\":\"john@example.com\",\"status\":\"ACTIVE\"}";
    }
}