package com.security.controller;

import com.security.model.UserPrincipal;
import com.security.model.Users;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) {
        System.out.println(user);
        return userService.verify(user);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Users> getUserProfile(@PathVariable Integer userId) {
        Users user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

//    Update user profile
    @PutMapping("/profile/{userId}")
    public ResponseEntity<Users> updateUserProfile(@PathVariable Integer userId, @RequestBody Users userDetails) {
        Users updatedUser = userService.updateUserProfile(userId, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

//    Delete user account
    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<?> deleteUserProfile(@PathVariable Integer userId, Authentication authentication) {

//        Extract User ID from Token
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Integer authenticatedUserId = userPrincipal.getId();
        System.out.println("Authenticated User ID: " + authenticatedUserId);
        System.out.println("Requested User ID: " + userId);

        if(!authenticatedUserId.equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this account");
        }
//        and now proceed with deleting the user profile
        userService.deleteUserProfile(userId);
        return ResponseEntity.ok().build();
    }

//    Users can change their password
    @PostMapping("/profile/{userId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Integer userId, @RequestBody Map<String, String> passwordMap) {
        userService.changePassword(userId, passwordMap.get("oldPassword"), passwordMap.get("newPassword"));
        return ResponseEntity.ok().build();
    }

//    List all users ( Admin functionality to list all users )
    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/test")
    public String see() {
        return new String("Now you are allowed to see details..");
    }

    @GetMapping("/home")
    public String sayHi() {
        return "Hi!";
    }

}
