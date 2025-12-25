package com.chat.chattingApp.repository;

import com.chat.chattingApp.entities.ChatRoom;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomRepo extends MongoRepository<ChatRoom, String> {
    public Optional<ChatRoom> findById(String roomId);
}
