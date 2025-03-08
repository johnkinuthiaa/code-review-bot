package com.slippery.codereview.service.impl;

import com.slippery.codereview.dto.UserDto;
import com.slippery.codereview.models.Users;
import com.slippery.codereview.repository.UserRepository;
import com.slippery.codereview.service.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder =new BCryptPasswordEncoder(12);
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository repository, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
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
        UserDto response =new UserDto();
        var user =repository.findById(userId);
        if(userId <0){
            response.setMessage("User id cannot be less than 0");
            response.setStatusCode(400);
            return response;
        }
        if(user.isEmpty()){
            response.setMessage("User does not exist ");
            response.setStatusCode(404);
            return response;
        }
        response.setMessage("User with id "+userId+" was found");
        response.setUser(user.get());
        response.setStatusCode(200);
        return response;
    }

    @Override
    public UserDto deleteUserById(Long userId) {
        UserDto response =new UserDto();
        var user =findUserById(userId);
        if(user.getStatusCode() !=200){
            return user;
        }
        repository.delete(user.getUser());
        response.setMessage("User with id "+userId+" deleted ");
        response.setStatusCode(200);
        return response;
    }

    @Override
    public UserDto updateUserById(Long userId, Users users) {
        UserDto response =new UserDto();
        var user =findUserById(userId);
        if(user.getStatusCode() !=200){
            return user;
        }
        return response;
    }

    @Override
    public UserDto login(Users loginDetails) {
        UserDto response =new UserDto();
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDetails.getUsername(),loginDetails.getPassword()));
            if(authentication.isAuthenticated()){
                response.setMessage("User "+loginDetails.getUsername()+ " logged in successfully");
                response.setStatusCode(200);
                return response;
            }
            response.setStatusCode(401);
            response.setMessage("User authentication failed");
        } catch (Exception e) {
            throw new BadCredentialsException(e.getLocalizedMessage());
        }
        return response;
    }
}
