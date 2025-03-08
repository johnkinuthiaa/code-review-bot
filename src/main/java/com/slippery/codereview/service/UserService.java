package com.slippery.codereview.service;

import com.slippery.codereview.dto.UserDto;
import com.slippery.codereview.models.Users;

public interface UserService {
    UserDto registerUser(Users user);
    UserDto findUserById(Long userId);
    UserDto deleteUserById(Long userId);
    UserDto updateUserById(Long userId,Users users);
    UserDto login(Users loginDetails);

}
