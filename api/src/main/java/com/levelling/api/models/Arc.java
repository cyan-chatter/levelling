package com.levelling.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "arcs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Arc {
    public enum ResetPeriod {
        DAILY, WEEKLY, MONTHLY, NEVER
    }
    @Id
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private int points; // Points for completing the arc itself
    private ResetPeriod resetPeriod = ResetPeriod.NEVER;
    private List<String> associatedTrackIds; // Handles many-to-many relationship
    private String createdBy; // User ID if user-created, null for preset
}