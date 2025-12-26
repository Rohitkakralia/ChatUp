package com.chat.chattingApp.repository;

import com.chat.chattingApp.entities.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepo extends MongoRepository<Message, ObjectId> {

    List<Message> findByRoomIdOrderByTimestampAsc(String roomId);
}
