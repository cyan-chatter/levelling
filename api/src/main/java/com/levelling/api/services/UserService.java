package com.levelling.api.services;

import com.levelling.api.dto.HistoryEventDto;
import com.levelling.api.dto.OwnedItemDto;
import com.levelling.api.models.*;
import com.levelling.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ArcRepository arcRepository;

    /**
     * Retrieves a list of all items owned by the user.
     * 
     * @param username The username of the logged-in user.
     * @return A list of DTOs representing the owned items.
     */
    public List<OwnedItemDto> getOwnedItems(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getOwnedItemIds() == null || user.getOwnedItemIds().isEmpty()) {
            return new ArrayList<>();
        }

        List<Item> ownedItems = itemRepository.findAllById(user.getOwnedItemIds());
        return ownedItems.stream().map(this::mapItemToDto).collect(Collectors.toList());
    }

    /**
     * Compiles a chronological history of the user's completed tasks and arcs.
     * 
     * @param username The username of the logged-in user.
     * @return A sorted list of history events, with the most recent first.
     */
    public List<HistoryEventDto> getHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserProgress progress = userProgressRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Progress not found for user"));

        List<HistoryEventDto> historyEvents = new ArrayList<>();

        // 1. Process completed tasks
        if (progress.getCompletedTasks() != null && !progress.getCompletedTasks().isEmpty()) {
            List<String> taskIds = progress.getCompletedTasks().stream()
                    .map(UserProgress.CompletedTask::getTaskId)
                    .toList();
            List<Task> tasks = taskRepository.findAllById(taskIds);
            var taskMap = tasks.stream().collect(Collectors.toMap(Task::getId, task -> task));

            for (UserProgress.CompletedTask completedTask : progress.getCompletedTasks()) {
                Task task = taskMap.get(completedTask.getTaskId());
                if (task != null) {
                    historyEvents.add(new HistoryEventDto("TASK", task.getName(), task.getPoints(),
                            completedTask.getCompletedAt()));
                }
            }
        }

        // 2. Process completed arcs
        if (progress.getCompletedArcs() != null && !progress.getCompletedArcs().isEmpty()) {
            List<String> arcIds = progress.getCompletedArcs().stream()
                    .map(UserProgress.CompletedArc::getArcId)
                    .toList();
            List<Arc> arcs = arcRepository.findAllById(arcIds);
            var arcMap = arcs.stream().collect(Collectors.toMap(Arc::getId, arc -> arc));

            for (UserProgress.CompletedArc completedArc : progress.getCompletedArcs()) {
                Arc arc = arcMap.get(completedArc.getArcId());
                if (arc != null) {
                    historyEvents.add(
                            new HistoryEventDto("ARC", arc.getName(), arc.getPoints(), completedArc.getCompletedAt()));
                }
            }
        }

        // 3. Sort by completion date, newest first
        historyEvents.sort(Comparator.comparing(HistoryEventDto::getCompletedAt).reversed());

        return historyEvents;
    }

    /**
     * Helper method to map an Item entity to an OwnedItemDto.
     * 
     * @param item The Item entity from the database.
     * @return The corresponding DTO.
     */
    private OwnedItemDto mapItemToDto(Item item) {
        OwnedItemDto dto = new OwnedItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setImageUrl(item.getImageUrl());
        dto.setRealWorldPrice(item.getRealWorldPrice());
        dto.setGamePrice(item.getGamePrice());
        dto.setTag(item.getTag());
        dto.setImpact(item.getImpact());
        dto.setStoreLink(item.getStoreLink());
        dto.setDescription(item.getDescription());
        return dto;
    }
}