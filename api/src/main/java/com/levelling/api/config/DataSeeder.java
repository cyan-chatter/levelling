package com.levelling.api.config;

import com.levelling.api.models.Arc;
import com.levelling.api.models.Item;
import com.levelling.api.models.Saga;
import com.levelling.api.models.Task;
import com.levelling.api.models.Track;
import com.levelling.api.repositories.ArcRepository;
import com.levelling.api.repositories.ItemRepository;
import com.levelling.api.repositories.SagaRepository;
import com.levelling.api.repositories.TaskRepository;
import com.levelling.api.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private SagaRepository sagaRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private ArcRepository arcRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void run(String... args) throws Exception {
        // Clean up previous data for a fresh seed
        if (sagaRepository.count() > 0) {
            taskRepository.deleteAll();
            arcRepository.deleteAll();
            trackRepository.deleteAll();
            sagaRepository.deleteAll();
            itemRepository.deleteAll();
        }

        System.out.println("Seeding database...");
        seedData();
        System.out.println("Database seeding complete.");
    }

    // Add this method to DataSeeder.java
    private void seedItems() {
        Item item1 = new Item(
            null,
            "Slick Black Hoodie",
            "/images/items/hoodie.jpg",
            45.00,
            convertPrice(45.00),
            "Clothing",
            "Fashion",
            "http://example.com/store/hoodie",
            "A stylish and comfortable black hoodie."
        );

        Item item2 = new Item(
            null,
            "RGB Mechanical Keyboard",
            "/images/items/keyboard.jpg",
            120.00,
            convertPrice(120.00),
            "Computer Accessory",
            "Utility",
            "http://example.com/store/keyboard",
            "A high-performance keyboard for gaming and productivity."
        );

        itemRepository.saveAll(List.of(item1, item2));
    }

    private Item.InGamePrice convertPrice(double realWorldPrice) {
        long points = (long) (realWorldPrice * 100);
        int gems = (int) Math.ceil(realWorldPrice / 10); // Simple conversion, one gem type for now
        return new Item.InGamePrice(points, gems, gems, 0, 0);
    }

    private void seedData() {
        // 1. Create Sagas
        Saga career = new Saga(null, "Career Saga", "Master your professional life.", "/images/sagas/career.jpg");
        Saga fitness = new Saga(null, "Fitness Saga", "Forge a strong and healthy body.", "/images/sagas/fitness.jpg");
        Saga social = new Saga(null, "Social Saga", "Nurture your relationships.", "/images/sagas/social.jpg");
        Saga hobbies = new Saga(null, "Hobbies Saga", "Cultivate your passions.", "/images/sagas/hobbies.jpg");
        Saga randoms = new Saga(null, "Randoms Saga", "A place for miscellaneous tracks.", "/images/sagas/randoms.jpg");
        sagaRepository.saveAll(List.of(career, fitness, social, hobbies, randoms));

        // 2. Create Tracks
        // Career Tracks
        Track webDev = new Track(null, "Web Dev", "Build for the modern web.", "/images/tracks/webdev.jpg",
                career.getId(), null);
        Track openSource = new Track(null, "Open Source", "Contribute to the community.",
                "/images/tracks/opensource.jpg", career.getId(), null);
        trackRepository.saveAll(List.of(webDev, openSource));

        // Fitness Tracks
        Track chest = new Track(null, "Chest", "Build a powerful chest.", "/images/tracks/chest.jpg", fitness.getId(),
                null);
        Track back = new Track(null, "Back", "Develop a strong back.", "/images/tracks/back.jpg", fitness.getId(),
                null);
        Track legs = new Track(null, "Legs", "Don't skip leg day.", "/images/tracks/legs.jpg", fitness.getId(), null);
        trackRepository.saveAll(List.of(chest, back, legs));

        // Social Tracks
        Track friends = new Track(null, "Friends", "Strengthen your bonds.", "/images/tracks/friends.jpg",
                social.getId(), null);
        Track partner = new Track(null, "Partner", "Grow together.", "/images/tracks/partner.jpg", social.getId(),
                null);
        trackRepository.saveAll(List.of(friends, partner));

        // Hobbies Tracks
        Track football = new Track(null, "Football", "The beautiful game.", "/images/tracks/football.jpg",
                hobbies.getId(), null);
        trackRepository.save(football);

        // 3. Create Arcs
        // Web Dev Arcs
        Arc htmlCssJs = new Arc(null, "HTML-CSS-JS", "The fundamentals of the web.", "/images/arcs/html.jpg", 100,
                List.of(webDev.getId()), null);
        Arc react = new Arc(null, "React", "Master the popular frontend library.", "/images/arcs/react.jpg", 150,
                List.of(webDev.getId()), null);
        Arc springBoot = new Arc(null, "Java Spring Boot", "Build robust backend APIs.", "/images/arcs/spring.jpg", 150,
                List.of(webDev.getId()), null);
        arcRepository.saveAll(List.of(htmlCssJs, react, springBoot));

        // Fitness Arcs
        Arc benchPress = new Arc(null, "Bench Press Mastery", "Master the king of upper body lifts.",
                "/images/arcs/bench.jpg", 50, List.of(chest.getId()), null);
        Arc deadlift = new Arc(null, "Deadlift Form", "Perfect your deadlift technique.", "/images/arcs/deadlift.jpg",
                50, List.of(back.getId(), legs.getId()), null); // Belongs to two tracks!
        arcRepository.saveAll(List.of(benchPress, deadlift));

        // 4. Create Tasks
        // HTML-CSS-JS Tasks
        taskRepository.save(
                new Task(null, "Complete a tutorial on Flexbox", Task.TaskType.PRIMARY, 20, htmlCssJs.getId(), null));
        taskRepository.save(new Task(null, "Build a simple responsive landing page", Task.TaskType.PRIMARY, 30,
                htmlCssJs.getId(), null));
        taskRepository
                .save(new Task(null, "Learn about the DOM", Task.TaskType.SECONDARY, 15, htmlCssJs.getId(), null));

        // React Tasks
        taskRepository.save(new Task(null, "Understand the difference between state and props", Task.TaskType.PRIMARY,
                25, react.getId(), null));
        taskRepository
                .save(new Task(null, "Build a To-Do List application", Task.TaskType.PRIMARY, 40, react.getId(), null));

        // Bench Press Tasks
        taskRepository.save(new Task(null, "Watch a tutorial on proper bench press form", Task.TaskType.PRIMARY, 10,
                benchPress.getId(), null));
        taskRepository.save(
                new Task(null, "Complete 3 sets of bench press", Task.TaskType.PRIMARY, 15, benchPress.getId(), null));
        taskRepository.save(new Task(null, "Perform a warm-up routine for chest", Task.TaskType.SECONDARY, 5,
                benchPress.getId(), null));

        seedItems();
    }
}