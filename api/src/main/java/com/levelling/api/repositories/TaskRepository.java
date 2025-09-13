package com.levelling.api.repositories;

import com.levelling.api.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {

    /**
     * Finds a Task by its unique name. Used by the DataSeeder.
     */
    Optional<Task> findByName(String name);

    /**
     * Finds all tasks for a given arc that are either preset (createdBy is null)
     * or were created by the specified user.
     */
    List<Task> findByArcIdAndCreatedByIsNullOrArcIdAndCreatedBy(String arcId, String arcIdAgain, String userId);

    /**
     * Finds all Task documents where the _id is in the provided list of string IDs.
     * This is more robust than findAllById for handling potential ObjectId/String
     * mismatches.
     */
    @Query("{ '_id': { '$in': ?0 } }")
    List<Task> findByIds(List<String> ids);
}