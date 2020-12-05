package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final CartRepository cartRepo = mock(CartRepository.class);
    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepo);
        TestUtils.injectObject(userController, "cartRepository", cartRepo);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath() {
        when(encoder.encode("abcdefg123")).thenReturn("123456abcde");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Sondos");
        r.setPassword("abcdefg123");
        r.setConfirmPassword("abcdefg123");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("Sondos", user.getUsername());
        assertEquals("123456abcde", user.getPassword());

    }

    @Test
    public void verifyGetUserByUsername() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Sondos");
        r.setPassword("abcdefg123");
        r.setConfirmPassword("abcdefg123");

        final ResponseEntity<User> response = userController.createUser(r);
        User user = response.getBody();
        when(userRepo.findByUsername(r.getUsername())).thenReturn(user);

        ResponseEntity<User> response2 = userController.findByUserName(r.getUsername());
        User user2 = response2.getBody();

        assertNotNull(user2);
        assertEquals("Sondos", user2.getUsername());
    }

    @Test
    public void verifyGetUserByUserId() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Sondos");
        r.setPassword("abcdefg123");
        r.setConfirmPassword("abcdefg123");

        final ResponseEntity<User> response = userController.createUser(r);
        User user = response.getBody();
        assertNotNull(user);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        ResponseEntity<User> response2 = userController.findById(user.getId());
        User user2 = response2.getBody();

        assertNotNull(user2);
        assertEquals(0, user2.getId());

    }
}