package com.levelling.api.controllers;

import com.levelling.api.models.Item;
import com.levelling.api.services.MarketplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/marketplace")
public class MarketplaceController {

    @Autowired
    private MarketplaceService marketplaceService;

    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(marketplaceService.getAllItems());
    }

    @PostMapping("/items/{itemId}/buy")
    public ResponseEntity<?> buyItem(Authentication authentication, @PathVariable String itemId) {
        String username = authentication.getName();
        marketplaceService.buyItem(username, itemId);
        return ResponseEntity.ok("Item purchased successfully.");
    }
}