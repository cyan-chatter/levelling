package com.levelling.api.controllers;

import com.levelling.api.models.Arc;
import com.levelling.api.models.Task;
import com.levelling.api.models.Track;
import com.levelling.api.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/tracks")
    public ResponseEntity<List<Track>> getTracksBySaga(Authentication authentication, @RequestParam String sagaId) {
        String username = authentication.getName();
        List<Track> tracks = contentService.getTracksBySagaId(sagaId, username);
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/arcs")
    public ResponseEntity<List<Arc>> getArcsByTrack(Authentication authentication, @RequestParam String trackId) {
        String username = authentication.getName();
        List<Arc> arcs = contentService.getArcsByTrackId(trackId, username);
        return ResponseEntity.ok(arcs);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasksByArc(Authentication authentication, @RequestParam String arcId) {
        String username = authentication.getName();
        List<Task> tasks = contentService.getTasksByArcId(arcId, username);
        return ResponseEntity.ok(tasks);
    }
}