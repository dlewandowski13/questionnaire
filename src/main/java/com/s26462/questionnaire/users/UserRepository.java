package com.s26462.questionnaire.users;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    @Query("{ username: ?0 }")
    Optional<User> findByUsername(String username);
}
