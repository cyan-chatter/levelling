package com.levelling.api.controllers;

import com.levelling.api.dto.CompleteArcDto;
import com.levelling.api.services.ProgressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<?> completeTask(Authentication authentication, @PathVariable String taskId) {
        String username = authentication.getName();
        progressService.completeTask(username, taskId);
        return ResponseEntity.ok("Task marked as complete.");
    }

    @PostMapping("/arcs/{arcId}/complete")
    public ResponseEntity<?> completeArc(Authentication authentication, @PathVariable String arcId,
            @Valid @RequestBody CompleteArcDto dto) {
        String username = authentication.getName();
        progressService.completeArc(username, arcId, dto.getHappinessLevel(), dto.getEffortLevel());
        return ResponseEntity.ok("Arc marked as complete.");
    }
}