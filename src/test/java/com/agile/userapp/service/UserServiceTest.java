package com.agile.userapp.service;

import com.agile.userapp.entity.User;
import com.agile.userapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setEmail("john@example.com");
        user1.setUsername("johndoe");
        user1.setGender("M");

        User user2 = new User();
        user1.setId(2L);
        user1.setName("Jane Doe");
        user1.setEmail("jane@example.com");
        user1.setUsername("janedoe");
        user1.setGender("M");

        List<User> users = Arrays.asList(user1, user2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<User> result = userService.findAllUsers(pageable);
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void testCreateUser() {
        User user1 = new User();
        user1.setId(null);
        user1.setName("John Doe");
        user1.setEmail("john@example.com");
        user1.setUsername("johndoe");
        user1.setGender("M");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John Doe");
        savedUser.setEmail("john@example.com");
        savedUser.setUsername("johndoe");
        savedUser.setGender("M");

        when(userRepository.save(user1)).thenReturn(savedUser);
        User result = userService.createUser(user1);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testUpdateUser() {

        User existingUser = new User();
        existingUser.setId(null);
        existingUser.setName("John Doe");
        existingUser.setEmail("john@example.com");
        existingUser.setUsername("johndoe");
        existingUser.setGender("M");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("John Smith");
        updatedUser.setEmail("johnsmith@example.com");
        updatedUser.setUsername("johnsmith");
        updatedUser.setGender("M");

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        User result = userService.updateUser("johndoe", updatedUser);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        verify(userRepository, times(1)).findByUsername("johndoe");
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testDeleteUser() {

        User existingUser = new User();
        existingUser.setId(null);
        existingUser.setName("John Doe");
        existingUser.setEmail("john@example.com");
        existingUser.setUsername("johndoe");
        existingUser.setGender("M");
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(existingUser));

        userService.deleteUser("johndoe");
        verify(userRepository, times(1)).findByUsername("johndoe");
    }

}
