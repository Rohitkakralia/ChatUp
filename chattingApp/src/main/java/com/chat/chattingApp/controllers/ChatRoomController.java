package com.chat.chattingApp.controllers;

import com.chat.chattingApp.entities.ChatRoom;
import com.chat.chattingApp.entities.Users;
import com.chat.chattingApp.repository.ChatRoomRepo;
import com.chat.chattingApp.services.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class ChatRoomController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private ChatRoomRepo chatRoomRepo;

    @PostMapping("/room/{friendEmail}")
    public ResponseEntity<?> chatRoom(@PathVariable String friendEmail) {
        try {
            // 1. Get the authenticated user's email
            String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("My email: " + myEmail);
            System.out.println("Friend email: " + friendEmail);

            // 2. Fetch both user objects
            Users user1 = userServices.findByEmail(myEmail);
            Users user2 = userServices.findByEmail(friendEmail);

            // Check if both users exist
            if (user1 == null) {
                return ResponseEntity.badRequest().body("Current user not found");
            }
            if (user2 == null) {
                return ResponseEntity.badRequest().body("Friend not found with email: " + friendEmail);
            }

            System.out.println("User1 ID: " + user1.getId());
            System.out.println("User2 ID: " + user2.getId());

            String userId1 = user1.getId().toString();
            String userId2 = user2.getId().toString();

            // 3. Generate a consistent Room ID
            String roomId = (userId1.compareTo(userId2) < 0)
                    ? userId1 + "_" + userId2
                    : userId2 + "_" + userId1;

            System.out.println("Generated Room ID: " + roomId);

            // 4. Find or Create the room
            ChatRoom room = chatRoomRepo.findById(roomId)
                    .orElseGet(() -> {
                        ChatRoom newRoom = new ChatRoom();
                        newRoom.setId(roomId);
                        newRoom.setUser1Id(user1.getId());
                        newRoom.setUser2Id(user2.getId());
                        System.out.println("Creating new chat room");
                        return chatRoomRepo.save(newRoom);
                    });

            System.out.println("Returning room: " + room.getId());
            return ResponseEntity.ok(room);

        } catch (Exception e) {
            e.printStackTrace(); // This will show you the actual error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
