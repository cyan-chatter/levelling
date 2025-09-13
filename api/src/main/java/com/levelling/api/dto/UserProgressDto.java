// src/main/java/com/levelling/api/dto/UserProgressDto.java
package com.levelling.api.dto;

import com.levelling.api.models.UserProgress;
import lombok.Data;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserProgressDto {
    private Set<String> completedTaskIds;
    private Set<String> completedArcIds;

    public UserProgressDto(UserProgress progress) {
        this.completedTaskIds = progress.getCompletedTasks().stream()
                .map(UserProgress.CompletedTask::getTaskId)
                .collect(Collectors.toSet());

        this.completedArcIds = progress.getCompletedArcs().stream() // <-- ADD THIS BLOCK
                .map(UserProgress.CompletedArc::getArcId)
                .collect(Collectors.toSet());
    }
}