package com.levelling.api.services;

import com.levelling.api.models.User;
import com.levelling.api.models.UserProgress;
import com.levelling.api.repositories.UserProgressRepository;
import com.levelling.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class ObjectiveService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProgressRepository userProgressRepository;

    public void setDailyObjectives(String username, List<String> taskIds) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserProgress progress = userProgressRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("User progress not found"));

        // We could add validation here to ensure taskIds are valid
        progress.setActiveDailyObjectiveTaskIds(new HashSet<>(taskIds));
        userProgressRepository.save(progress);
    }

    public void setWeeklyObjectives(String username, List<String> arcIds) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserProgress progress = userProgressRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("User progress not found"));

        // We could add validation here to ensure arcIds are valid
        progress.setActiveWeeklyObjectiveArcIds(new HashSet<>(arcIds));
        userProgressRepository.save(progress);
    }
}