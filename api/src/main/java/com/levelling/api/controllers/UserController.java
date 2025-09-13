package com.levelling.api.controllers;

import com.levelling.api.dto.HistoryEventDto;
import com.levelling.api.dto.OwnedItemDto;
import com.levelling.api.models.User;
import com.levelling.api.repositories.UserRepository;
import com.levelling.api.services.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/me/inventory")
    public ResponseEntity<List<OwnedItemDto>> getOwnedItems(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getOwnedItems(username));
    }

    @GetMapping("/me/history")
    public ResponseEntity<List<HistoryEventDto>> getHistory(Authentication authentication) {
        return ResponseEntity.ok(userService.getHistory(authentication.getName()));
    }
    
}