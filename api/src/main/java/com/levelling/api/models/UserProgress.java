package com.levelling.api.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.time.LocalDateTime;

@Document(collection = "user_progress")
@Data
@NoArgsConstructor
public class UserProgress {

    @Id
    private String id;

    private String userId; // Link to the User

    private List<CompletedTask> completedTasks = new ArrayList<>();;
    private List<CompletedArc> completedArcs = new ArrayList<>();
    private DailyStats dailyStats = new DailyStats();
    private WeeklyStats weeklyStats = new WeeklyStats();
    private Set<String> activeDailyObjectiveTaskIds = new HashSet<>();
    private Set<String> activeWeeklyObjectiveArcIds = new HashSet<>();

    // --- INNER CLASSES ---

    @Data
    @NoArgsConstructor
    public static class CompletedTask {
        private String taskId;
        private LocalDateTime completedAt;

        public CompletedTask(String taskId) {
            this.taskId = taskId;
            this.completedAt = LocalDateTime.now();
        }
    }

    @Data
    @NoArgsConstructor
    public static class CompletedArc {
        private String arcId;
        private LocalDateTime completedAt;
        private int happinessLevel;
        private int effortLevel;

        public CompletedArc(String arcId, int happinessLevel, int effortLevel) {
            this.arcId = arcId;
            this.completedAt = LocalDateTime.now();
            this.happinessLevel = happinessLevel;
            this.effortLevel = effortLevel;
        }
    }

    @Data
    @NoArgsConstructor
    public static class DailyStats {
        private LocalDate date = LocalDate.now();
        private int totalHappiness = 0;
        private int totalEffort = 0;
        private int regularityPoints = 0;
    }

    @Data
    @NoArgsConstructor
    public static class WeeklyStats {
        private LocalDate weekStartDate = getStartOfWeek();
        private int skillPoints = 0;

        // *** CHANGED FROM 'private' TO 'public' ***
        public static LocalDate getStartOfWeek() {
            // This ensures the week starts on Monday
            TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();
            return LocalDate.now().with(fieldISO, 1);
        }
    }

    public UserProgress(String userId) {
        this.userId = userId;
    }
}