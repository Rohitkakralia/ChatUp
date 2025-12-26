package com.chat.chattingApp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "room_time_idx", def = "{'roomId': 1, 'timestamp': -1}")
public class Message {

    @Id
    private ObjectId id;

    private String roomId; // Reference to ChatRoom

    private ObjectId senderId; // Who sent the message

    private ObjectId receiverId; // Who receives the message

    private String content;

//    private MessageType messageType = MessageType.TEXT;
//
//    private MessageStatus status = MessageStatus.SENT;

    private LocalDateTime timestamp = LocalDateTime.now();

    // For file/image messages
   // private String fileUrl;

    //private String fileName;

    // If message is deleted
    private boolean isDeleted = false;

    // If message is edited
    private boolean isEdited = false;

    private LocalDateTime editedAt;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public ObjectId getSenderId() {
        return senderId;
    }

    public void setSenderId(ObjectId senderId) {
        this.senderId = senderId;
    }

    public ObjectId getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(ObjectId receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }
}