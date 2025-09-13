package com.levelling.api.repositories;

import com.levelling.api.models.Arc;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ArcRepository extends MongoRepository<Arc, String> {

    /**
     * Finds all arcs associated with a given track that are either preset
     * (createdBy is null)
     * or were created by the specified user.
     * 
     * @param trackId      The ID of the track.
     * @param trackIdAgain The ID of the track, repeated for the OR clause.
     * @param userId       The ID of the user.
     * @return A list of relevant arcs.
     */
    List<Arc> findByAssociatedTrackIdsContainingAndCreatedByIsNullOrAssociatedTrackIdsContainingAndCreatedBy(
            String trackId, String trackIdAgain, String userId);
}