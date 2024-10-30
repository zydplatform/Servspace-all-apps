package com.kiganda.auth.controller;


import com.kiganda.auth.model.User;
import com.kiganda.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String tokens = authService.login(user);
        if (tokens != null) {
            return ResponseEntity.ok(tokens);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody String refreshToken) {
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        if (newAccessToken != null) {
            return ResponseEntity.ok(newAccessToken);
        }
        return ResponseEntity.status(401).body("Invalid refresh token");
    }

    @GetMapping("/secure")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> secureEndpoint() {
        return ResponseEntity.ok("This is a secure endpoint!");
    }
}
