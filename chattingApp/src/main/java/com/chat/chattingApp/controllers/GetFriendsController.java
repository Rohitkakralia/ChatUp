package com.chat.chattingApp.controllers;

import com.chat.chattingApp.entities.UserDTO;
import com.chat.chattingApp.entities.Users;
import com.chat.chattingApp.services.GetFriendsServices;
import com.chat.chattingApp.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class GetFriendsController {

    @Autowired
    private GetFriendsServices getFriendsServices;
    @Autowired
    private UserServices userServices;

    @GetMapping()
    public ResponseEntity<?> getFriends(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<Users> friends = getFriendsServices.getUserFriends(userEmail);
        return friends==null? null: ResponseEntity.ok(friends);
    }

    @PostMapping("/add-friend")
    public ResponseEntity<?> addFriend(@RequestParam String friendEmail) {
        try {
            // This is the "myEmail" extracted securely from the JWT token
            if(!userServices.existsByEmail(friendEmail)) return new ResponseEntity<>("User Email does not exist", HttpStatus.NOT_FOUND);
            String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            getFriendsServices.addFriend(myEmail, friendEmail);
            return ResponseEntity.ok("Friend added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/searchFriend")
    public ResponseEntity<?> searchFriend(@RequestParam String friendEmail) {
        try {
            Users user = getFriendsServices.searchUsers(friendEmail);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User does not exist");
            }

            // Return only name and email
            UserDTO userDTO = new UserDTO(user.getName(), user.getEmail());
            return ResponseEntity.ok(userDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
