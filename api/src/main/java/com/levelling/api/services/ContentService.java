package com.levelling.api.services;

import com.levelling.api.models.*;
import com.levelling.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.levelling.api.dto.TaskSelectionDto;
import java.util.Map;

@Service
public class ContentService {

        @Autowired
        private TrackRepository trackRepository;
        @Autowired
        private ArcRepository arcRepository;
        @Autowired
        private TaskRepository taskRepository;
        @Autowired
        private UserRepository userRepository;

        public List<Track> getTracksBySagaId(String sagaId, String username) {
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                return trackRepository.findBySagaIdAndCreatedByIsNullOrSagaIdAndCreatedBy(sagaId, sagaId, user.getId());
        }

        public List<Arc> getArcsByTrackId(String trackId, String username) {
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                return arcRepository
                                .findByAssociatedTrackIdsContainingAndCreatedByIsNullOrAssociatedTrackIdsContainingAndCreatedBy(
                                                trackId, trackId, user.getId());
        }

        public List<Task> getTasksByArcId(String arcId, String username) {
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                return taskRepository.findByArcIdAndCreatedByIsNullOrArcIdAndCreatedBy(arcId, arcId, user.getId());
        }

        // --- CORRECTED METHODS ---

        public List<TaskSelectionDto> getAllPresetTasks() {
                // 1. Get all preset tasks
                List<Task> allTasks = taskRepository.findAll().stream()
                                .filter(task -> task.getCreatedBy() == null)
                                .toList();

                // 2. Get all unique Arc IDs from those tasks
                List<String> arcIds = allTasks.stream().map(Task::getArcId).distinct().toList();

                // 3. Fetch all the corresponding Arcs in a single query
                List<Arc> allArcs = arcRepository.findByIds(arcIds);

                // 4. Create a fast lookup map of Arc ID -> Arc Name
                Map<String, String> arcIdToNameMap = allArcs.stream()
                                .collect(Collectors.toMap(Arc::getId, Arc::getName));

                // 5. Build the final DTOs
                return allTasks.stream()
                                .map(task -> new TaskSelectionDto(task,
                                                arcIdToNameMap.getOrDefault(task.getArcId(), "Unknown Arc")))
                                .toList();
        }

        public List<Arc> getAllPresetArcs() {
                return arcRepository.findAll().stream()
                                // Applying the same safe filtering logic here
                                .filter(arc -> Objects.nonNull(arc) && Objects.isNull(arc.getCreatedBy()))
                                .collect(Collectors.toList());
        }
}