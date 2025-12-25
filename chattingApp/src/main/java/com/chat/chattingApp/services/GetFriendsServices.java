package com.chat.chattingApp.services;

import com.chat.chattingApp.entities.Users;
import com.chat.chattingApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetFriendsServices {
    @Autowired
    private UserRepo userRepo;

    public List<Users> getUserFriends(String email){
        Users user = userRepo.findByEmail(email);
        return user.getFriends();
    }

    public void addFriend(String myEmail, String friendEmail) {
        Users me = userRepo.findByEmail(myEmail);
        Users friend = userRepo.findByEmail(friendEmail);

        if (me == null || friend == null) {
            throw new RuntimeException("User or Friend not found");
        }

        // Prevent adding yourself
        if (myEmail.equals(friendEmail)) {
            throw new RuntimeException("You cannot add yourself");
        }

        // Add friend to my list
        me.addFriend(friend);

        // Add me to friend's list (Mutual friendship)
        friend.addFriend(me);

        // Save both
        userRepo.save(me);
        userRepo.save(friend);
    }

    public Users searchUsers(String query) {
        return userRepo.findByEmail(query);
    }
}


