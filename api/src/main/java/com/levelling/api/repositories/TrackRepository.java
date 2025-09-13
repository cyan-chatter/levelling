package com.levelling.api.repositories;

import com.levelling.api.models.Track;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TrackRepository extends MongoRepository<Track, String> {

    /**
     * Finds a Track by its unique name. Used by the DataSeeder.
     */
    Optional<Track> findByName(String name);

    /**
     * Finds all tracks for a given saga that are either preset (createdBy is null)
     * or were created by the specified user.
     */
    List<Track> findBySagaIdAndCreatedByIsNullOrSagaIdAndCreatedBy(String sagaId, String sagaIdAgain, String userId);
}