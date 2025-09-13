package com.levelling.api.repositories;

import com.levelling.api.models.UserProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserProgressRepository extends MongoRepository<UserProgress, String> {
    Optional<UserProgress> findByUserId(String userId);
}