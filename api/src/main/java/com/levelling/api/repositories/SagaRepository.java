package com.levelling.api.repositories;

import com.levelling.api.models.Saga;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SagaRepository extends MongoRepository<Saga, String> {

    /**
     * Finds a Saga by its unique name. Used by the DataSeeder.
     */
    Optional<Saga> findByName(String name);
}