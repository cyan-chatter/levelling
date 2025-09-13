package com.levelling.api.services;

import com.levelling.api.models.Saga;
import com.levelling.api.repositories.SagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SagaService {

    @Autowired
    private SagaRepository sagaRepository;

    public List<Saga> getAllSagas() {
        return sagaRepository.findAll();
    }
}