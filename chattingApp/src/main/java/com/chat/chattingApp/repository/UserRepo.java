package com.chat.chattingApp.repository;

import com.chat.chattingApp.entities.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<Users, ObjectId> {
    Users findByEmail(String email);
    // Finds users by name for the search feature
    java.util.List<Users> findByNameContainingIgnoreCase(String name);
}
