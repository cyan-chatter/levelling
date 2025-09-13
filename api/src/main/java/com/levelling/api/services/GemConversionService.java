package com.levelling.api.services;

import com.levelling.api.models.User;
import com.levelling.api.models.UserProgress;
import com.levelling.api.repositories.UserProgressRepository;
import com.levelling.api.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GemConversionService {

    private static final Logger logger = LoggerFactory.getLogger(GemConversionService.class);

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Runs every day at 23:57 (11:57 PM) to convert Happiness and Effort points.
     */
    @Scheduled(cron = "0 57 23 * * *")
    @Transactional
    public void convertDailyPointsToGems() {
        logger.info("Starting daily conversion of Happiness and Effort points to Gems.");
        List<UserProgress> allProgress = userProgressRepository.findAll();

        for (UserProgress progress : allProgress) {
            User user = userRepository.findById(progress.getUserId()).orElse(null);
            if (user == null)
                continue;

            // Convert Happiness -> Yellow Gems
            int happinessPoints = progress.getDailyStats().getTotalHappiness();
            int yellowGemsEarned = calculateGemsFromPoints(happinessPoints);
            if (yellowGemsEarned > 0) {
                user.getCurrencies().setYellowGems(user.getCurrencies().getYellowGems() + yellowGemsEarned);
            }

            // Convert Effort -> Royal Blue Gems
            int effortPoints = progress.getDailyStats().getTotalEffort();
            int blueGemsEarned = calculateGemsFromPoints(effortPoints);
            if (blueGemsEarned > 0) {
                user.getCurrencies().setRoyalBlueGems(user.getCurrencies().getRoyalBlueGems() + blueGemsEarned);
            }

            if (yellowGemsEarned > 0 || blueGemsEarned > 0) {
                logger.info("User '{}': +{} Yellow Gems, +{} Royal Blue Gems.", user.getUsername(), yellowGemsEarned,
                        blueGemsEarned);
                userRepository.save(user);

                String message = String.format(
                        "Daily conversion complete! You earned %d Yellow Gems and %d Royal Blue Gems.",
                        yellowGemsEarned, blueGemsEarned);
                notificationService.createNotification(user.getId(), message);
            }
        }
        logger.info("Daily gem conversion finished.");
    }

    /**
     * Runs every Sunday at 23:58 (11:58 PM) to convert Regularity points.
     */
    @Scheduled(cron = "0 58 23 * * SUN")
    @Transactional
    public void convertWeeklyPointsToGems() {
        logger.info("Starting weekly conversion of Regularity points to Crimson Red Gems.");
        List<UserProgress> allProgress = userProgressRepository.findAll();

        for (UserProgress progress : allProgress) {
            User user = userRepository.findById(progress.getUserId()).orElse(null);
            if (user == null)
                continue;

            int regularityPoints = progress.getDailyStats().getRegularityPoints();
            int redGemsEarned = calculateGemsFromPoints(regularityPoints); // Using the same threshold logic
            if (redGemsEarned > 0) {
                user.getCurrencies().setCrimsonRedGems(user.getCurrencies().getCrimsonRedGems() + redGemsEarned);
                logger.info("User '{}': +{} Crimson Red Gems.", user.getUsername(), redGemsEarned);
                userRepository.save(user);

                String message = String.format("Weekly Regularity bonus! You earned %d Crimson Red Gems.",
                        redGemsEarned);
                notificationService.createNotification(user.getId(), message);
            }
        }
        logger.info("Weekly gem conversion finished.");
    }

    /**
     * Runs on the last day of every month at 23:59 (11:59 PM).
     */
    @Scheduled(cron = "0 59 23 L * *")
    @Transactional
    public void convertMonthlyPointsToGems() {
        logger.info("Starting monthly conversion of Skill points to Emerald Green Gems.");
        List<UserProgress> allProgress = userProgressRepository.findAll();

        for (UserProgress progress : allProgress) {
            User user = userRepository.findById(progress.getUserId()).orElse(null);
            if (user == null)
                continue;

            int skillPoints = progress.getWeeklyStats().getSkillPoints();
            int greenGemsEarned = calculateGemsFromPoints(skillPoints); // Using the same threshold logic
            if (greenGemsEarned > 0) {
                user.getCurrencies().setEmeraldGreenGems(user.getCurrencies().getEmeraldGreenGems() + greenGemsEarned);
                logger.info("User '{}': +{} Emerald Green Gems.", user.getUsername(), greenGemsEarned);
                userRepository.save(user);

                String message = String.format("Monthly Skill bonus! You earned %d Emerald Green Gems.",
                        greenGemsEarned);
                notificationService.createNotification(user.getId(), message);
            }
        }
        logger.info("Monthly gem conversion finished.");
    }

    private int calculateGemsFromPoints(int points) {
        if (points >= 200)
            return 5;
        if (points >= 100)
            return 4;
        if (points >= 50)
            return 3;
        if (points >= 25)
            return 2;
        if (points >= 10)
            return 1;
        return 0;
    }
}