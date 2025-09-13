package com.levelling.api.services;

import com.levelling.api.models.Notification;
import com.levelling.api.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Creates a new notification for a user. This will be called by other services.
     * 
     * @param userId  The ID of the user to notify.
     * @param message The message content.
     */
    public void createNotification(String userId, String message) {
        Notification notification = new Notification(userId, message);
        notificationRepository.save(notification);
    }

    /**
     * Retrieves all notifications for a given user.
     * 
     * @param userId The ID of the user.
     * @return A list of notifications, newest first.
     */
    public List<Notification> getNotificationsForUser(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Marks all unread notifications for a user as read.
     * 
     * @param userId The ID of the user.
     */
    public void markAllAsRead(String userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .filter(n -> !n.isRead())
                .toList();

        if (!unreadNotifications.isEmpty()) {
            unreadNotifications.forEach(n -> n.setRead(true));
            notificationRepository.saveAll(unreadNotifications);
        }
    }
}