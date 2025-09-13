// src/main/java/com/levelling/api/dto/TaskSelectionDto.java
package com.levelling.api.dto;

import com.levelling.api.models.Task;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskSelectionDto {
    private String id;
    private String name;
    private int points;
    private String arcName;
    private String trackName; // We'll add this later if needed

    public TaskSelectionDto(Task task, String arcName) {
        this.id = task.getId();
        this.name = task.getName();
        this.points = task.getPoints();
        this.arcName = arcName;
    }
}