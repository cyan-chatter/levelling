package com.levelling.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    private String id;
    private String name;
    private String imageUrl;
    private double realWorldPrice; // e.g., 49.99
    private InGamePrice gamePrice; // Converted price
    private String tag; // "Clothing", "Accessory", etc.
    private String impact; // "Fashion", "Utility", etc.
    private String storeLink; // Optional
    private String description; // Optional

    // Inner class for the in-game currency price
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InGamePrice {
        private long points;
        private int yellowGems;
        private int royalBlueGems;
        private int crimsonRedGems;
        private int emeraldGreenGems;
    }
}