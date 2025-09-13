package com.levelling.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    public enum TaskType {
        PRIMARY, // Compulsory
        SECONDARY // Optional
    }

    public enum PermanenceType {
        PERMANENT, TEMPORARY
    }

    @Id
    private String id;
    private String name;
    private TaskType type;
    private PermanenceType permanenceType = PermanenceType.PERMANENT; // Default to PERMANENT
    private int points;
    private String arcId; // The ID of the parent Arc
    private String createdBy; // User ID if user-created, null for preset
}