package com.example.noteapp.backend.service;

import com.example.noteapp.backend.entity.User;
import com.example.noteapp.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // テストデータの準備
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testSaveUser() {
        // ユーザーを保存
        User savedUser = userService.save(testUser);

        // 検証
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindAll() {
        // テストデータを保存
        userService.save(testUser);

        // 全ユーザーを取得
        List<User> users = userService.findAll();

        // 検証
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo("testuser");
    }

    @Test
    void testFindById() {
        // テストデータを保存
        User savedUser = userService.save(testUser);

        // IDで検索
        Optional<User> foundUser = userService.findById(savedUser.getId());

        // 検証
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void testDeleteById() {
        // テストデータを保存
        User savedUser = userService.save(testUser);

        // ユーザーを削除
        userService.deleteById(savedUser.getId());

        // 検証
        Optional<User> deletedUser = userService.findById(savedUser.getId());
        assertThat(deletedUser).isEmpty();
    }
} 