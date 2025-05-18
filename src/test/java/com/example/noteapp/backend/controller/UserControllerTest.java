package com.example.noteapp.backend.controller;

import com.example.noteapp.backend.entity.User;
import com.example.noteapp.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        now = LocalDateTime.now();
    }

    private User createTestUser(Long id, String username, String email) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password");
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setStatus(User.UserStatus.ACTIVE);
        return user;
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(
            createTestUser(1L, "user1", "email1@example.com"),
            createTestUser(2L, "user2", "email2@example.com")
        );
        when(userService.findAll()).thenReturn(expectedUsers);

        // Act
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedUsers, response.getBody());
        verify(userService, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        User expectedUser = createTestUser(userId, "user1", "email1@example.com");
        when(userService.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        ResponseEntity<User> response = userController.getUserById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedUser, response.getBody());
        verify(userService, times(1)).findById(userId);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        Long userId = 999L;
        when(userService.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.getUserById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(userService, times(1)).findById(userId);
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        // Arrange
        User userToCreate = createTestUser(null, "newUser", "newuser@example.com");
        User createdUser = createTestUser(1L, "newUser", "newuser@example.com");
        when(userService.save(userToCreate)).thenReturn(createdUser);

        // Act
        ResponseEntity<User> response = userController.createUser(userToCreate);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(createdUser, response.getBody());
        verify(userService, times(1)).save(userToCreate);
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userService).deleteById(userId);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(userId);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        verify(userService, times(1)).deleteById(userId);
    }
} 