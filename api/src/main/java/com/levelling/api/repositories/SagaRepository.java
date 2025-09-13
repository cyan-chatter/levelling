package com.levelling.api.repositories;

import com.levelling.api.models.Saga;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SagaRepository extends MongoRepository<Saga, String> {
}