package com.travelq.service;

import com.travelq.domain.model.UserEntity;
import com.travelq.domain.repository.UserRepository;
import com.travelq.domain.service.logic.UserServiceImpl;
import com.travelq.dto.UserDto;
import com.travelq.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto createUserDto(Long id) {
        return new UserDto(
                id,
                "testuser",
                "password123",
                "user@test.com",
                "Arsene",
                "Marinel",
                null,
                null,
                null
        );
    }

    private UserEntity createUserEntity(Long id) {
        return new UserEntity(
                id,
                "testuser",
                "password123",
                "user@test.com",
                "Arsene",
                "Marinel",
                new ArrayList<>(),
                null,
                new ArrayList<>()
        );
    }

    @Test
    void testAddUser() {
        UserDto dto = createUserDto(null);
        UserEntity entity = createUserEntity(null);
        UserEntity savedEntity = createUserEntity(1L);
        UserDto savedDto = createUserDto(1L);

        when(modelMapper.map(dto, UserEntity.class)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, UserDto.class)).thenReturn(savedDto);

        UserDto result = userService.addUser(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).save(entity);
    }

    @Test
    void testGetAllUsers() {
        List<UserEntity> entities = Arrays.asList(createUserEntity(1L), createUserEntity(2L));
        when(userRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(any(UserEntity.class), eq(UserDto.class)))
                .thenAnswer(invocation -> {
                    UserEntity source = invocation.getArgument(0);
                    return createUserDto(source.getId());
                });

        List<UserDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserById_Found() {
        UserEntity entity = createUserEntity(1L);
        UserDto dto = createUserDto(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, UserDto.class)).thenReturn(dto);

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void testUpdateUser_Found() {
        Long userId = 1L;
        UserDto userDto = createUserDto(userId);
        UserEntity existingEntity = createUserEntity(userId);
        UserEntity updatedEntity = createUserEntity(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingEntity));
        doNothing().when(modelMapper).map(userDto, existingEntity);
        when(userRepository.save(existingEntity)).thenReturn(updatedEntity);
        when(modelMapper.map(updatedEntity, UserDto.class)).thenReturn(userDto);

        UserDto result = userService.updateUser(userId, userDto);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void testUpdateUser_NotFound() {
        Long userId = 404L;
        UserDto userDto = createUserDto(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, userDto));
    }

    @Test
    void testDeleteUser_Exists() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_NotExists() {
        Long userId = 999L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
    }
}
