package com.slippery.codereview.service.impl;

import com.slippery.codereview.dto.UserDto;
import com.slippery.codereview.models.Users;
import com.slippery.codereview.repository.UserRepository;
import com.slippery.codereview.service.UserService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder =new BCryptPasswordEncoder(12);

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDto registerUser( Users user) {
        UserDto response =new UserDto();
        var existingUser =repository.findByUsername(user.getUsername());
        if(existingUser!=null){
            response.setMessage("User already exists");
            response.setStatusCode(401);
            return response;
        }
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRequestLimits(20);
            user.setGuest(false);
            repository.save(user);
            response.setMessage("New user created");
            response.setStatusCode(201);
            response.setUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    public UserDto findUserById(Long userId) {
        return null;
    }

    @Override
    public UserDto deleteUserById(Long userId) {
        return null;
    }

    @Override
    public UserDto updateUserById(Long userId, Users users) {
        return null;
    }
}
