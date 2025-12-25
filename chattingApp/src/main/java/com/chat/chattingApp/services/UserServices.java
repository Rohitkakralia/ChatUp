package com.chat.chattingApp.services;


import com.chat.chattingApp.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.chat.chattingApp.repository.UserRepo;

@Service
public class UserServices {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users findByEmail(String email){
        return userRepo.findByEmail(email);
    }

    public boolean existsByEmail(String email){
        Users user =  userRepo.findByEmail(email);
        if(user != null) return true;
        else return false;
    }

    public Users createUser(Users userInfo) {

        Users newUser = new Users();
        newUser.setName(userInfo.getName());
        newUser.setEmail(userInfo.getEmail());
        newUser.setPassword(passwordEncoder.encode(userInfo.getPassword()));

        return userRepo.save(newUser);
    }


    public boolean canLogin(Users loginReq) {
        // 1. Get the user from the DB
        Users user = userRepo.findByEmail(loginReq.getEmail());

        // 2. Use the .matches() method provided by Spring Security
        // Syntax: matches(rawPassword, encodedPasswordFromDB)
        return passwordEncoder.matches(loginReq.getPassword(), user.getPassword());
    }
}
