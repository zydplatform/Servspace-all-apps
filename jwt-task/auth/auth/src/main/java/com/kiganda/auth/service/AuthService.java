package com.kiganda.auth.service;


import com.kiganda.auth.model.User;
import com.kiganda.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final Map<String, String> users = new HashMap<>(); // In-memory user store

    @Autowired
    private JwtUtil jwtUtil;

    public AuthService() {
        // Sample users
        users.put("user1", "password1");
        users.put("user2", "password2");
    }

    public String login(User user) {
        String storedPassword = users.get(user.getUsername());
        if (storedPassword != null && storedPassword.equals(user.getPassword())) {
            String accessToken = jwtUtil.generateAccessToken(user.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
            return accessToken + ":" + refreshToken; // Return both tokens
        }
        return null; // Invalid credentials
    }

    public String refreshAccessToken(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);
        if (username != null && jwtUtil.validateToken(refreshToken, username)) {
            return jwtUtil.generateAccessToken(username);
        }
        return null; // Invalid refresh token
    }
}
