package com.levelling.api.services;

import com.levelling.api.models.*;
import com.levelling.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    @Autowired
    private UserProgressRepository userProgressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ArcRepository arcRepository;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public void completeTask(String username, String taskId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        UserProgress progress = userProgressRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("User progress not found"));

        // Check if task is already completed to prevent duplicate rewards and history
        // entries
        boolean isAlreadyCompleted = progress.getCompletedTasks().stream()
                .anyMatch(ct -> ct.getTaskId().equals(taskId));

        if (!isAlreadyCompleted) {
            // Add the task to the list of completed tasks with a timestamp
            progress.getCompletedTasks().add(new UserProgress.CompletedTask(taskId));

            // Add standard points to user's currency
            user.getCurrencies().setPoints(user.getCurrencies().getPoints() + task.getPoints());

            // Check if the completed task is a daily objective and award regularity points
            if (progress.getActiveDailyObjectiveTaskIds().contains(taskId)) {
                resetDailyStatsIfNewDay(progress);
                int regularityReward = task.getPoints(); // Or a custom value
                progress.getDailyStats().setRegularityPoints(
                        progress.getDailyStats().getRegularityPoints() + regularityReward);
            }

            userRepository.save(user);
            userProgressRepository.save(progress);
        }
    }

    @Transactional
    public void completeArc(String username, String arcId, int happiness, int effort) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Arc arc = arcRepository.findById(arcId)
                .orElseThrow(() -> new RuntimeException("Arc not found"));
        UserProgress progress = userProgressRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("User progress not found"));

        // --- Validation: Check if all primary tasks are done ---
        List<Task> primaryTasks = taskRepository
                .findByArcIdAndCreatedByIsNullOrArcIdAndCreatedBy(arcId, arcId, user.getId()).stream()
                .filter(task -> task.getType() == Task.TaskType.PRIMARY)
                .toList();

        Set<String> completedTaskIds = progress.getCompletedTasks().stream()
                .map(UserProgress.CompletedTask::getTaskId)
                .collect(Collectors.toSet());

        boolean allPrimaryTasksCompleted = primaryTasks.stream()
                .allMatch(task -> completedTaskIds.contains(task.getId()));

        if (!allPrimaryTasksCompleted) {
            throw new RuntimeException("Cannot complete Arc: Not all primary tasks are finished.");
        }
        // --- End Validation ---

        boolean isAlreadyCompleted = progress.getCompletedArcs().stream()
                .anyMatch(completedArc -> completedArc.getArcId().equals(arcId));

        if (!isAlreadyCompleted) {
            // Check for "First Arc" achievement before adding the new arc to the list
            if (progress.getCompletedArcs().isEmpty()) {
                String message = String.format(
                        "Achievement Unlocked: Arc Beginner! You completed your first arc: '%s'.", arc.getName());
                notificationService.createNotification(user.getId(), message);
            }

            // Add points for completing the arc itself
            user.getCurrencies().setPoints(user.getCurrencies().getPoints() + arc.getPoints());

            // Update daily stats for happiness and effort
            resetDailyStatsIfNewDay(progress);
            progress.getDailyStats().setTotalHappiness(progress.getDailyStats().getTotalHappiness() + happiness);
            progress.getDailyStats().setTotalEffort(progress.getDailyStats().getTotalEffort() + effort);

            // Check if the completed arc is a weekly objective and award skill points
            if (progress.getActiveWeeklyObjectiveArcIds().contains(arcId)) {
                resetWeeklyStatsIfNewWeek(progress);
                int skillPointReward = arc.getPoints(); // Or a custom value
                progress.getWeeklyStats().setSkillPoints(
                        progress.getWeeklyStats().getSkillPoints() + skillPointReward);
            }

            userRepository.save(user);

            // Add arc to the list of completed arcs with a timestamp
            progress.getCompletedArcs().add(new UserProgress.CompletedArc(arcId, happiness, effort));
            userProgressRepository.save(progress);
        }
    }

    private void resetDailyStatsIfNewDay(UserProgress progress) {
        if (!progress.getDailyStats().getDate().equals(LocalDate.now())) {
            progress.setDailyStats(new UserProgress.DailyStats());
        }
    }

    private void resetWeeklyStatsIfNewWeek(UserProgress progress) {
        LocalDate currentWeekStartDate = UserProgress.WeeklyStats.getStartOfWeek();
        if (!progress.getWeeklyStats().getWeekStartDate().equals(currentWeekStartDate)) {
            progress.setWeeklyStats(new UserProgress.WeeklyStats());
        }
    }
}