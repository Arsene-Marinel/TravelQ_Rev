package com.travelq.service;

import com.travelq.domain.service.UserService;
import com.travelq.dto.UserDto;
import com.travelq.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTestProfileTest {

    @Autowired
    private UserService userService;

    private UserDto baseUser;

    @BeforeEach
    public void setUp() {
        baseUser = new UserDto();
        baseUser.setUsername("john_doe");
        baseUser.setEmail("john@example.com");
        baseUser.setFirstName("John");
        baseUser.setLastName("Doe");
        baseUser.setPassword("password123");
    }

    @Test
    public void testAddUser() {
        UserDto createdUser = userService.addUser(baseUser);

        assertNotNull(createdUser.getId());
        assertEquals(baseUser.getUsername(), createdUser.getUsername());
        assertEquals(baseUser.getEmail(), createdUser.getEmail());
    }

    @Test
    public void testGetAllUsers() {
        userService.addUser(baseUser);
        List<UserDto> users = userService.getAllUsers();
        assertFalse(users.isEmpty());
    }

    @Test
    public void testGetUserById() {
        UserDto createdUser = userService.addUser(baseUser);
        UserDto foundUser = userService.getUserById(createdUser.getId());

        assertEquals(createdUser.getUsername(), foundUser.getUsername());
        assertEquals(createdUser.getEmail(), foundUser.getEmail());
    }

    @Test
    public void testUpdateUser() {
        UserDto created = userService.addUser(baseUser);

        UserDto updateData = new UserDto();
        updateData.setUsername("updated");
        updateData.setEmail("updated@example.com");
        updateData.setFirstName("Updated");
        updateData.setLastName("User");
        updateData.setPassword("newpass");

        UserDto updated = userService.updateUser(created.getId(), updateData);

        assertEquals("updated", updated.getUsername());
        assertEquals("updated@example.com", updated.getEmail());
    }

    @Test
    public void testDeleteUser() {
        UserDto createdUser = userService.addUser(baseUser);
        userService.deleteUser(createdUser.getId());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(createdUser.getId()));
    }
}
