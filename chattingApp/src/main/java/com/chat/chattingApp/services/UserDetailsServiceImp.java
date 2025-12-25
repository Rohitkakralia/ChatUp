package com.chat.chattingApp.services;

import com.chat.chattingApp.entities.Users;
import com.chat.chattingApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        System.out.println("Hit loadUserByUsername");
        Users user = userRepo.findByEmail(email);

        if(user != null){
            UserDetails userDetails = User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .build();

            return userDetails;
        }

        throw new UsernameNotFoundException("User not found with username : " + email);

    }
}
