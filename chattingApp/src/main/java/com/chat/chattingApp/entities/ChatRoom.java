package com.chat.chattingApp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "user_pair_idx", def = "{'user1Id': 1, 'user2Id': 1}", unique = true)
public class ChatRoom {

    @Id
    private String id;

    // Store user IDs in sorted order to ensure uniqueness
    private ObjectId user1Id; // Smaller ObjectId
    private ObjectId user2Id; // Larger ObjectId

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime lastMessageTime;

    private String lastMessage;

    // Helper method to create a chat room ID from two user IDs
    public static String generateRoomId(ObjectId userId1, ObjectId userId2) {
        // Ensure consistent ordering
        if (userId1.compareTo(userId2) < 0) {
            return userId1.toString() + "_" + userId2.toString();
        } else {
            return userId2.toString() + "_" + userId1.toString();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectId getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(ObjectId user1Id) {
        this.user1Id = user1Id;
    }

    public ObjectId getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(ObjectId user2Id) {
        this.user2Id = user2Id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}

