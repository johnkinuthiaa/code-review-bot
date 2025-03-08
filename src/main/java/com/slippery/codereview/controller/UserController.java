package com.slippery.codereview.controller;

import com.slippery.codereview.dto.UserDto;
import com.slippery.codereview.models.Users;
import com.slippery.codereview.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody Users user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }
    @GetMapping("/{userId}/get")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findUserById(userId));
    }
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<UserDto> deleteUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }
    @PutMapping("/{userId}/update")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Long userId, @RequestBody Users users) {
        return ResponseEntity.ok(userService.updateUserById(userId, users));
    }
    @PutMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody Users loginDetails){
        return ResponseEntity.ok(userService.login(loginDetails));
    }
}
