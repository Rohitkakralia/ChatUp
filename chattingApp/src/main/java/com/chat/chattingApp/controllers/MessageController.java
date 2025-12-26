package com.chat.chattingApp.controllers;

import com.chat.chattingApp.entities.Message;
import com.chat.chattingApp.entities.MessageDTO;
import com.chat.chattingApp.entities.Users;
import com.chat.chattingApp.services.MessageServices;
import com.chat.chattingApp.services.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private MessageServices messageServices;

    @PostMapping("/send")
    public ResponseEntity<?> receivedMessage(@RequestBody MessageDTO messageDTO){
        try {
            String senderEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Users sender = userServices.findByEmail(senderEmail);
            ObjectId senderId = sender.getId();

            Users receiver = userServices.findByEmail(messageDTO.getReceiverEmail());
            ObjectId receiverId = receiver.getId();

            return messageServices.addMessage(messageDTO.getMessage(), messageDTO.getRoomId(), senderId, receiverId);

        }catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<Message>> getChatHistory(@PathVariable String roomId) {
        List<Message> messages = messageServices.getMessagesByRoom(roomId);
        return ResponseEntity.ok(messages);
    }
}
