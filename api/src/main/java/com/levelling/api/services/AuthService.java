package com.levelling.api.services;

import com.levelling.api.dto.LoginDto;
import com.levelling.api.dto.RegisterDto;
import com.levelling.api.models.User;
import com.levelling.api.models.UserProgress;
import com.levelling.api.repositories.UserProgressRepository;
import com.levelling.api.repositories.UserRepository;
import com.levelling.api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public User registerUser(RegisterDto registerDto) {
        // Check if username already exists
        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        // Check if email already exists
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Assign a default role
        user.setRoles(Collections.singletonList("ROLE_USER"));
        
        // TESTING PURPOSES - GIVE NEW USERS A BUNCH OF CURRENCY
        user.getCurrencies().setPoints(50000);
        user.getCurrencies().setYellowGems(100);
        user.getCurrencies().setRoyalBlueGems(100);

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Create a corresponding progress document for the new user
        UserProgress userProgress = new UserProgress(savedUser.getId());
        userProgressRepository.save(userProgress);

        return savedUser;
    }

    public String loginUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }
}