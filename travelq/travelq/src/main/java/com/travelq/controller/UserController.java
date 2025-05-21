package com.travelq.controller;

import com.travelq.dto.StandardResponse;
import com.travelq.dto.UserDto;
import com.travelq.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Register a new user", description = "Creates a new user account with encrypted password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        log.info("Registration request received for username: {}", userDto.getUsername());
        // Encode the password before saving
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserDto createdUser = userService.addUser(userDto);
        log.info("User registered successfully with ID: {}", createdUser.getId());
        return ResponseEntity.ok(createdUser);
    }

    @Operation(summary = "Create a user", description = "Creates a new user with admin access")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Create user request received for username: {}", userDto.getUsername());
        UserDto createdUser = userService.addUser(userDto);
        log.info("User created successfully with ID: {}", createdUser.getId());
        return ResponseEntity.ok(createdUser);
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully", 
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Request to fetch all users");
        List<UserDto> users = userService.getAllUsers();
        log.info("Returning {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {
        log.info("Request to fetch user with ID: {}", id);
        UserDto user = userService.getUserById(id);
        log.info("User found with ID: {}", id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update user", description = "Updates an existing user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "User ID", required = true) @PathVariable Long id, 
            @Valid @RequestBody UserDto userDto) {
        log.info("Request to update user with ID: {}", id);
        UserDto updatedUser = userService.updateUser(id, userDto);
        log.info("User updated successfully with ID: {}", id);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user", description = "Deletes a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteUser(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {
        log.info("Request to delete user with ID: {}", id);
        userService.deleteUser(id);
        log.info("User deleted successfully with ID: {}", id);
        return ResponseEntity.ok(StandardResponse.success("User with ID " + id + " was deleted."));
    }
}
