package com.levelling.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HistoryEventDto {
    private String type; // "TASK" or "ARC"
    private String name;
    private int points;
    private LocalDateTime completedAt;
}