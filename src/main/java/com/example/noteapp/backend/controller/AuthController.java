package com.example.noteapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.noteapp.backend.repository.UserRepository;
import com.example.noteapp.backend.util.JwtUtil;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        return userRepository.findByUsername(username)  
            .filter(user -> passwordEncoder.matches(password, user.getPassword()))
            .map(user -> {
                String token = jwtUtil.generateToken(user.getUsername());
                return ResponseEntity.ok(Map.of("token", token));
            })
            .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }
}
