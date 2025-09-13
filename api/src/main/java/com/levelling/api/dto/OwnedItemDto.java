package com.levelling.api.dto;

import com.levelling.api.models.Item;
import lombok.Data;

@Data
public class OwnedItemDto {
    private String id;
    private String name;
    private String imageUrl;
    private double realWorldPrice;
    private Item.InGamePrice gamePrice;
    private String tag;
    private String impact;
    private String storeLink;
    private String description;
}