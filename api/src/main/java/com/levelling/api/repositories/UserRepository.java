package com.levelling.api.repositories;

import com.levelling.api.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds a user by their username. Used for login and registration checks.
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email. Used for login and registration checks.
     */
    Optional<User> findByEmail(String email);
}