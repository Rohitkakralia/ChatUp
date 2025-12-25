package com.chat.chattingApp.repository;

import com.chat.chattingApp.entities.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepo extends MongoRepository<Message, ObjectId> {
}
