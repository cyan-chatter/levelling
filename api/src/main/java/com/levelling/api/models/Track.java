package com.levelling.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tracks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    @Id
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String sagaId; // We store the ID of the parent saga
    private String createdBy; // User ID if user-created, null for preset
}