package com.levelling.api.repositories;

import com.levelling.api.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    /**
     * Finds all notifications for a specific user, sorted by creation date in
     * descending order (newest first).
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
}