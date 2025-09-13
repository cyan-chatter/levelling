package com.levelling.api.controllers;

import com.levelling.api.models.Saga;
import com.levelling.api.services.SagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sagas")
public class SagaController {

    @Autowired
    private SagaService sagaService;

    @GetMapping
    public ResponseEntity<List<Saga>> getAllSagas() {
        return ResponseEntity.ok(sagaService.getAllSagas());
    }
}