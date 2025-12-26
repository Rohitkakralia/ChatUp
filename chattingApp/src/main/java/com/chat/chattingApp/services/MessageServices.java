package com.chat.chattingApp.services;

import com.chat.chattingApp.entities.ChatRoom;
import com.chat.chattingApp.entities.Message;
import com.chat.chattingApp.repository.ChatRoomRepo;
import com.chat.chattingApp.repository.MessageRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServices {
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private ChatRoomRepo chatRoomRepo;

    public ResponseEntity<?> addMessage(String message, String roomId, ObjectId senderId, ObjectId receiverId) {
        try{
            if(!chatRoomRepo.existsById(roomId)){
                return new ResponseEntity<>("Room Id not present", HttpStatus.NOT_FOUND);
            }

            Message newMessage = new Message();
            newMessage.setRoomId(roomId);
            newMessage.setContent(message);
            newMessage.setSenderId(senderId);
            newMessage.setReceiverId(receiverId);

            messageRepo.save(newMessage);

            // 2. Update the ChatRoom
            Optional<ChatRoom> roomOpt = chatRoomRepo.findById(roomId);
            if (roomOpt.isPresent()) {
                ChatRoom room = roomOpt.get();
                room.setLastMessage(message);
                room.setLastMessageTime(LocalDateTime.now());
                chatRoomRepo.save(room);
            }

            return new ResponseEntity<>(newMessage, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public List<Message> getMessagesByRoom(String roomId) {
        return messageRepo.findByRoomIdOrderByTimestampAsc(roomId);
    }
}
