package com.levelling.api.services;

import com.levelling.api.models.Arc;
import com.levelling.api.models.Task;
import com.levelling.api.models.Track;
import com.levelling.api.models.User;
import com.levelling.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .findByAssociatedTrackIdsContainingAndCreatedByIsNullOrAssociatedTrackIdsContainingAndCreatedBy(trackId,
                        trackId, user.getId());
    }

    public List<Task> getTasksByArcId(String arcId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return taskRepository.findByArcIdAndCreatedByIsNullOrArcIdAndCreatedBy(arcId, arcId, user.getId());
    }
}