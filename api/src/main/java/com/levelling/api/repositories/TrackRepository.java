package com.levelling.api.repositories;

import com.levelling.api.models.Track;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TrackRepository extends MongoRepository<Track, String> {

    /**
     * Finds all tracks for a given saga that are either preset (createdBy is null)
     * or were created by the specified user.
     * 
     * @param sagaId      The ID of the saga.
     * @param sagaIdAgain The ID of the saga, repeated for the OR clause.
     * @param userId      The ID of the user.
     * @return A list of relevant tracks.
     */
    List<Track> findBySagaIdAndCreatedByIsNullOrSagaIdAndCreatedBy(String sagaId, String sagaIdAgain, String userId);
}