package com.chat.chattingApp.controllers;



import com.chat.chattingApp.entities.Users;
import com.chat.chattingApp.services.EmailService;
import com.chat.chattingApp.services.UserDetailsServiceImp;
import com.chat.chattingApp.utilis.JwtUtil;
import com.chat.chattingApp.utilis.OtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.chat.chattingApp.services.UserServices;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/public/users")
@Slf4j
public class UserController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping()
    public String checker(){
        return "It's working";
    }

//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@RequestBody Users userInfo) {
//        try {
//            // Check if user already exists
//            if (userServices.existsByEmail(userInfo.getEmail())) {
//                System.out.println("User already exist");
//                return new ResponseEntity<>( "user already exist", HttpStatus.FOUND);
//            }
//
//            // Create new user
//            Users user = userServices.createUser(userInfo);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "User registered successfully!");
//            response.put("userId", user.getId());
//            response.put("name", user.getName());
//            response.put("email", user.getEmail());
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, String> response = new HashMap<>();
//            response.put("error", "Registration failed: " + e.getMessage());
//            return ResponseEntity.internalServerError().body(response);
//        }
//    }

    private final Map<String, String> otpStore = new HashMap<>();
    private final Map<String, Users> tempUserStore = new HashMap<>();


    @PostMapping("/signup/request")
    public ResponseEntity<?> requestSignup(@RequestBody Users user) {
        System.out.println("Hit /signup/request");

        if(userServices.existsByEmail(user.getEmail())){
            return new ResponseEntity<>("User Already exist! Go for login", HttpStatus.BAD_REQUEST);
        }
        // 1. Generate OTP
        String otp = OtpUtil.generateOtp(6);

        // 2. Send OTP to email
        emailService.sendOtp(user.getEmail(), otp);

        // 3. Store OTP (temporary - use DB/Redis ideally)
        otpStore.put(user.getEmail(), otp);

        // 4. Store user temporarily (in-memory for now)
        tempUserStore.put(user.getEmail(), user); // Map<String, Users>

        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent to email.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup/verify")
    public ResponseEntity<String> verifyOtpAndSignup(@RequestParam String email, @RequestParam String otp) {
        System.out.println("Received OTP: " + otp + " for email: " + email);
        String storedOtp = otpStore.get(email);

        if (storedOtp != null && storedOtp.equals(otp)) {
            Users user = tempUserStore.get(email);
            if (user != null) {
                userServices.createUser(user);

                // Clean up
                otpStore.remove(email);
                tempUserStore.remove(email);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                String jwt = jwtUtil.generateToken(userDetails);

                System.out.println("JWT GENERATED: " + jwt);

                return new ResponseEntity<>(jwt, HttpStatus.CREATED);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }




    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Users loginReq){
        try{
            //This will automatically check weather users information is correct or not
            if(!userServices.existsByEmail(loginReq.getEmail())){
                return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
            }
            System.out.println("in loginUser");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginReq.getEmail());
            String jwt = jwtUtil.generateToken(userDetails);//This will regenerate jwt - This class is made by us
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occurs while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username and password", HttpStatus.UNAUTHORIZED);
        }
    }
}