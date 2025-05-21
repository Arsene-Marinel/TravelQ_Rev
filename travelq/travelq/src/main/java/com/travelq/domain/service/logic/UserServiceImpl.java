package com.travelq.domain.service.logic;

import com.travelq.dto.UserDto;
import com.travelq.exception.UserNotFoundException;
import com.travelq.domain.model.UserEntity;
import com.travelq.domain.repository.UserRepository;
import com.travelq.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        log.info("Creating new user with username: {}", userDto.getUsername());
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        UserEntity saved = userRepository.save(userEntity);
        log.info("User created successfully with ID: {}", saved.getId());
        return modelMapper.map(saved, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Fetching all users");
        List<UserEntity> userList = userRepository.findAll();
        return userList.stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Fetching user with ID: {}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new UserNotFoundException(userId);
                });
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("Updating user with ID: {}", userId);
        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for update with ID: {}", userId);
                    return new UserNotFoundException(userId);
                });

        modelMapper.map(userDto, existingUser); // suprascrie câmpurile

        UserEntity updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            log.error("User not found for deletion with ID: {}", userId);
            throw new UserNotFoundException(userId);
        }
        userRepository.deleteById(userId);
        log.info("User deleted successfully with ID: {}", userId);
    }
}
