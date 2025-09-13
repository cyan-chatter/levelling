package com.levelling.api.repositories;

import com.levelling.api.models.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ItemRepository extends MongoRepository<Item, String> {

    /**
     * Finds an Item by its unique name. Used by the DataSeeder.
     */
    Optional<Item> findByName(String name);
}