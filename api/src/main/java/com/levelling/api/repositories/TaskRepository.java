package com.levelling.api.repositories;

import com.levelling.api.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

    // OLD METHOD (we can remove or keep it if needed elsewhere)
    // List<Task> findByArcId(String arcId);

    // NEW, BETTER METHOD
    /**
     * Finds all tasks for a given arc that are either preset (createdBy is null)
     * or were created by the specified user.
     * 
     * @param arcId  The ID of the arc.
     * @param userId The ID of the user.
     * @return A list of relevant tasks.
     */
    List<Task> findByArcIdAndCreatedByIsNullOrArcIdAndCreatedBy(String arcId, String arcIdAgain, String userId);
}