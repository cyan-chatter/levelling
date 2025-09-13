package com.levelling.api.services;

import com.levelling.api.models.Arc;
import com.levelling.api.models.Task;
import com.levelling.api.models.User;
import com.levelling.api.models.UserProgress;
import com.levelling.api.repositories.ArcRepository;
import com.levelling.api.repositories.TaskRepository;
import com.levelling.api.repositories.UserProgressRepository;
import com.levelling.api.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ObjectiveService {

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProgressRepository userProgressRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ArcRepository arcRepository;

    // We need MongoTemplate for this low-level debugging
    @Autowired
    private MongoTemplate mongoTemplate;


    // --- SET METHODS ARE UNCHANGED ---
    public void setDailyObjectives(String username, List<String> taskIds) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserProgress progress = userProgressRepository.findByUserId(user.getId()).orElseGet(() -> new UserProgress(user.getId()));
        progress.setActiveDailyObjectiveTaskIds(new HashSet<>(taskIds));
        userProgressRepository.save(progress);
        logger.info("Successfully saved daily objectives for user '{}'.", username);
    }

    public void setWeeklyObjectives(String username, List<String> arcIds) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserProgress progress = userProgressRepository.findByUserId(user.getId()).orElseGet(() -> new UserProgress(user.getId()));
        progress.setActiveWeeklyObjectiveArcIds(new HashSet<>(arcIds));
        userProgressRepository.save(progress);
        logger.info("Successfully saved weekly objectives for user '{}'.", username);
    }


    // --- GET METHOD WITH DEBUGGING LOGIC ---

    public List<Task> getDailyObjectives(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<UserProgress> progressOpt = userProgressRepository.findByUserId(user.getId());

        if (progressOpt.isEmpty()) return new ArrayList<>();
        UserProgress progress = progressOpt.get();
        if (progress.getActiveDailyObjectiveTaskIds() == null || progress.getActiveDailyObjectiveTaskIds().isEmpty()) return new ArrayList<>();
        
        List<String> objectiveIds = new ArrayList<>(progress.getActiveDailyObjectiveTaskIds());

        // --- START OF DEBUGGING BLOCK ---
        logger.info("====================== DEBUGGING OBJECTIVES ======================");
        logger.info("Objective IDs stored in user_progress document: {}", objectiveIds);

        if (!objectiveIds.isEmpty()) {
            // Get the first ID from the user's progress to check its type
            String firstObjectiveId = objectiveIds.get(0);
            logger.info("Type of the ID stored in user_progress: {}", firstObjectiveId.getClass().getName());

            // Now, let's find a task in the main `tasks` collection to compare
            Query query = new Query();
            query.limit(1); // We only need one example
            Task exampleTask = mongoTemplate.findOne(query, Task.class);
            
            if (exampleTask != null) {
                String idFromTaskCollection = exampleTask.getId();
                // To get the raw BSON type, we do a more complex query
                Query idQuery = new Query(Criteria.where("_id").is(idFromTaskCollection));
                var rawDocument = mongoTemplate.findOne(idQuery, org.bson.Document.class, "tasks");
                if (rawDocument != null) {
                    Object rawId = rawDocument.get("_id");
                    logger.info("Example _id from the 'tasks' collection: {}", rawId.toString());
                    logger.info("RAW BSON Type of the _id in 'tasks' collection: {}", rawId.getClass().getName());
                }
            } else {
                logger.warn("Could not find any example task in the 'tasks' collection to debug.");
            }
        }
        logger.info("==================================================================");
        // --- END OF DEBUGGING BLOCK ---

        return taskRepository.findByIds(objectiveIds);
    }


    public List<Arc> getWeeklyObjectives(String username) {
        // (Weekly objectives logic remains the same for this test)
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<UserProgress> progressOpt = userProgressRepository.findByUserId(user.getId());
        if (progressOpt.isEmpty()) return new ArrayList<>();
        UserProgress progress = progressOpt.get();
        if (progress.getActiveWeeklyObjectiveArcIds() == null || progress.getActiveWeeklyObjectiveArcIds().isEmpty()) return new ArrayList<>();
        return arcRepository.findByIds(new ArrayList<>(progress.getActiveWeeklyObjectiveArcIds()));
    }
}