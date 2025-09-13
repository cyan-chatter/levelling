package com.levelling.api.repositories;

import com.levelling.api.models.Arc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArcRepository extends MongoRepository<Arc, String> {

    /**
     * Finds an Arc by its unique name. Used by the DataSeeder.
     */
    Optional<Arc> findByName(String name);

    /**
     * Finds all arcs associated with a given track that are either preset
     * (createdBy is null)
     * or were created by the specified user.
     */
    List<Arc> findByAssociatedTrackIdsContainingAndCreatedByIsNullOrAssociatedTrackIdsContainingAndCreatedBy(
            String trackId, String trackIdAgain, String userId);

    /**
     * Finds all Arc documents where the _id is in the provided list of string IDs.
     * This is more robust than findAllById for handling potential ObjectId/String
     * mismatches.
     */
    @Query("{ '_id': { '$in': ?0 } }")
    List<Arc> findByIds(List<String> ids);
}