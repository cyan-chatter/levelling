package com.levelling.api.controllers;

import com.levelling.api.dto.JwtAuthResponseDto;
import com.levelling.api.dto.LoginDto;
import com.levelling.api.dto.RegisterDto;
import com.levelling.api.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        try {
            authService.registerUser(registerDto);
            return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> loginUser(@Valid @RequestBody LoginDto loginDto) {
        String token = authService.loginUser(loginDto);
        JwtAuthResponseDto jwtAuthResponse = new JwtAuthResponseDto(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }
}