package com.levelling.api.controllers;

import com.levelling.api.dto.SetObjectivesDto;
import com.levelling.api.services.ObjectiveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/objectives")
public class ObjectiveController {

    @Autowired
    private ObjectiveService objectiveService;

    @PostMapping("/daily")
    public ResponseEntity<?> setDailyObjectives(Authentication authentication,
            @Valid @RequestBody SetObjectivesDto dto) {
        String username = authentication.getName();
        objectiveService.setDailyObjectives(username, dto.getIds());
        return ResponseEntity.ok("Daily objectives have been set.");
    }

    @PostMapping("/weekly")
    public ResponseEntity<?> setWeeklyObjectives(Authentication authentication,
            @Valid @RequestBody SetObjectivesDto dto) {
        String username = authentication.getName();
        objectiveService.setWeeklyObjectives(username, dto.getIds());
        return ResponseEntity.ok("Weekly objectives have been set.");
    }
}