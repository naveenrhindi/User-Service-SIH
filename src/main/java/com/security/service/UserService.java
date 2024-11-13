package com.security.service;

import com.security.model.Users;
import com.security.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public Users register(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole("ROLE_USER"); // set default role
        user.setRole(user.getRole());
        return userRepo.save(user);
    }

    public Users getUserProfile(Integer userId) {
        return userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String verify(Users user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
        );
        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());

        return "Fail";
    }

    public Users updateUserProfile(Integer userId, Users userDetails) {

        Users existingUser = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Update only non-sensitive fields
         existingUser.setUsername(userDetails.getUsername());
         existingUser.setEmail(userDetails.getEmail());

//        existingUser.setUsername(userDetails.getUsername());
//        existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
//        existingUser.setEmail(userDetails.getEmail());
        return userRepo.save(existingUser);
    }

    public void deleteUserProfile(Integer userId) {
        Users user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found!"));
        userRepo.delete(user);
    }

    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        Users user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found!"));
        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new RuntimeException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }
}
