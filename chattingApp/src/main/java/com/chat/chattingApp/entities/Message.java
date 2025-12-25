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

    private ObjectId roomId; // Reference to ChatRoom

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
}