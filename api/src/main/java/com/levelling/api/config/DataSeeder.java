package com.levelling.api.config;

import com.levelling.api.models.*;
import com.levelling.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired private SagaRepository sagaRepository;
    @Autowired private TrackRepository trackRepository;
    @Autowired private ArcRepository arcRepository;
    @Autowired private TaskRepository taskRepository;
    @Autowired private ItemRepository itemRepository;

    @Override
    public void run(String... args) throws Exception {
        // This is the core change. We only run the seeder if the database is empty.
        if (sagaRepository.count() == 0) {
            System.out.println("No data found. Seeding database...");
            seedData();
            System.out.println("Database seeding complete.");
        } else {
            System.out.println("Data already exists. Skipping seeding.");
        }
    }

    private void seedData() {
        // 1. Create Sagas
        Saga career = createSagaIfNotFound("Career Saga", "Master your professional life.", "/images/sagas/career.jpg");
        Saga fitness = createSagaIfNotFound("Fitness Saga", "Forge a strong and healthy body.", "/images/sagas/fitness.jpg");
        Saga social = createSagaIfNotFound("Social Saga", "Nurture your relationships.", "/images/sagas/social.jpg");
        Saga hobbies = createSagaIfNotFound("Hobbies Saga", "Cultivate your passions.", "/images/sagas/hobbies.jpg");
        createSagaIfNotFound("Randoms Saga", "A place for miscellaneous tracks.", "/images/sagas/randoms.jpg");

        // 2. Create Tracks (passing the parent Saga object)
        Track webDev = createTrackIfNotFound("Web Dev", "Build for the modern web.", "/images/tracks/webdev.jpg", career, null);
        createTrackIfNotFound("Open Source", "Contribute to the community.", "/images/tracks/opensource.jpg", career, null);

        Track chest = createTrackIfNotFound("Chest", "Build a powerful chest.", "/images/tracks/chest.jpg", fitness, null);
        Track back = createTrackIfNotFound("Back", "Develop a strong back.", "/images/tracks/back.jpg", fitness, null);
        Track legs = createTrackIfNotFound("Legs", "Don't skip leg day.", "/images/tracks/legs.jpg", fitness, null);

        createTrackIfNotFound("Friends", "Strengthen your bonds.", "/images/tracks/friends.jpg", social, null);
        createTrackIfNotFound("Partner", "Grow together.", "/images/tracks/partner.jpg", social, null);
        
        createTrackIfNotFound("Football", "The beautiful game.", "/images/tracks/football.jpg", hobbies, null);

        // 3. Create Arcs (passing parent Track objects)
        Arc htmlCssJs = createArcIfNotFound("arc-html", "HTML-CSS-JS", "...", null, 100, List.of(webDev),
                        Arc.ResetPeriod.NEVER, null);
        Arc react = createArcIfNotFound("arc-react", "React", "...", null, 150, List.of(webDev), Arc.ResetPeriod.NEVER, null);

        // This is a repeatable workout arc
        Arc benchPress = createArcIfNotFound("arc-bench", "Bench Press Mastery", "...", null, 50, List.of(chest),
                        Arc.ResetPeriod.WEEKLY, null);
        Arc deadlift = createArcIfNotFound("arc-deadlift", "Deadlift Form", "...", null, 50, List.of(back, legs),
                        Arc.ResetPeriod.WEEKLY, null);

        // 4. Create Tasks (passing parent Arc objects)
        createTaskIfNotFound("Complete a tutorial on Flexbox", Task.TaskType.PRIMARY, 20, htmlCssJs, null);
        createTaskIfNotFound("Build a simple responsive landing page", Task.TaskType.PRIMARY, 30, htmlCssJs, null);
        createTaskIfNotFound("Learn about the DOM", Task.TaskType.SECONDARY, 15, htmlCssJs, null);
        
        createTaskIfNotFound("Understand the difference between state and props", Task.TaskType.PRIMARY, 25, react, null);
        createTaskIfNotFound("Build a To-Do List application", Task.TaskType.PRIMARY, 40, react, null);

        createTaskIfNotFound("Watch a tutorial on proper bench press form", Task.TaskType.PRIMARY, 10, benchPress, null);
        createTaskIfNotFound("Complete 3 sets of bench press", Task.TaskType.PRIMARY, 15, benchPress, null);
        createTaskIfNotFound("Perform a warm-up routine for chest", Task.TaskType.SECONDARY, 5, benchPress, null);

        createTaskIfNotFound("Watch a tutorial on proper deadlift form", Task.TaskType.PRIMARY, 10, deadlift, null);

        // 5. Create Marketplace Items
        seedItems();
    }
    
    // --- Helper Methods to prevent duplicate creation ---

    private Saga createSagaIfNotFound(String name, String description, String imageUrl) {
        return sagaRepository.findByName(name).orElseGet(() -> 
            sagaRepository.save(new Saga(null, name, description, imageUrl)));
    }

    private Track createTrackIfNotFound(String name, String desc, String img, Saga saga, String createdBy) {
        return trackRepository.findByName(name).orElseGet(() -> 
            trackRepository.save(new Track(null, name, desc, img, saga.getId(), createdBy)));
    }

    private Arc createArcIfNotFound(String id, String name, String desc, String img, int points, List<Track> tracks,
                    Arc.ResetPeriod resetPeriod, String createdBy) {
            List<String> trackIds = tracks.stream().map(Track::getId).toList();
            return arcRepository.findById(id).orElseGet(() -> {
                    Arc newArc = new Arc(id, name, desc, img, points, resetPeriod, trackIds, createdBy);
                    return arcRepository.save(newArc);
            });
    }

    private void createTaskIfNotFound(String name, Task.TaskType type, int points, Arc arc, String createdBy) {
        taskRepository.findByName(name).orElseGet(() -> 
            taskRepository.save(new Task(null, name, type, null, points, arc.getId(), createdBy)));
    }

    private void createItemIfNotFound(String name, String img, double realPrice, String tag, String impact, String link, String desc) {
        itemRepository.findByName(name).orElseGet(() -> 
            itemRepository.save(new Item(null, name, img, realPrice, convertPrice(realPrice), tag, impact, link, desc)));
    }
    
    // --- Marketplace Seeding Logic ---

    private void seedItems() {
        createItemIfNotFound("Slick Black Hoodie", "/images/items/hoodie.jpg", 45.00, "Clothing", "Fashion", "http://example.com/store/hoodie", "A stylish and comfortable black hoodie.");
        createItemIfNotFound("RGB Mechanical Keyboard", "/images/items/keyboard.jpg", 120.00, "Computer Accessory", "Utility", "http://example.com/store/keyboard", "A high-performance keyboard for gaming and productivity.");
    }

    private Item.InGamePrice convertPrice(double realWorldPrice) {
        long points = (long) (realWorldPrice * 100);
        int gems = (int) Math.ceil(realWorldPrice / 10);
        return new Item.InGamePrice(points, gems, gems, 0, 0);
    }
}