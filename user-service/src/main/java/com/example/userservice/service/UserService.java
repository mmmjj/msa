package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.vo.ResponseUser;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    ResponseUser getUserByUserId(String userId);
    List<ResponseUser> getUserByAll();
}
