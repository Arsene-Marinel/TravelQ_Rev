package com.travelq.domain.service;

import com.travelq.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);
}
