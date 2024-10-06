package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.ERole;
import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import static org.mockito.Mockito.when;
class UserServiceTest {

    @Mock
    IUserRepository iUserRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteUser() {
        Long userId = 1L;

        doNothing().when(iUserRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(iUserRepository, times(1)).deleteById(userId);
    }

    @Test
    void updatePassword_WhenOldPasswordIsCorrect() {
        Long userId = 1L;
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        BCryptPasswordEncoder realPasswordEncoder = new BCryptPasswordEncoder();
        String encodedOldPassword = realPasswordEncoder.encode(oldPassword);
        String encodedNewPassword = realPasswordEncoder.encode(newPassword);

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setPassword(encodedOldPassword);

        when(iUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(oldPassword, encodedOldPassword)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
        when(iUserRepository.save(mockUser)).thenReturn(mockUser);

        User updatedUser = userService.updatePassword(oldPassword, newPassword, userId);

        assertNotNull(updatedUser);
        assertTrue(realPasswordEncoder.matches(newPassword, updatedUser.getPassword()));
        verify(iUserRepository).save(mockUser);
    }

    @Test
    void updatePassword_WhenOldPasswordIsIncorrect() {
        Long userId = 1L;
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String encodedOldPassword = "encodedOldPassword";

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setPassword(encodedOldPassword);

        when(iUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(oldPassword, encodedOldPassword)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(oldPassword, newPassword, userId);
        });

        assertEquals("Old password is incorrect", exception.getMessage());
        verify(iUserRepository, never()).save(mockUser);
    }

    @Test
    void getUserById() {
        ERole eRole = ERole.USER;
        User user = new User();
        user.setId(1L);
        user.setUsername("Angel");
        user.setPassword("1234");
        user.setEmail("angel@gmail.com");
        user.setRole(eRole);

        when(iUserRepository.findById(1L)).thenReturn(Optional.of(user));

        User response = userService.getUserById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Angel", response.getUsername());
    }


    @Test
    void updateRole() {
        ERole oldRole = ERole.USER;
        ERole newRole = ERole.ADMIN;

        User user = new User();
        user.setId(1L);
        user.setUsername("Angel");
        user.setRole(oldRole);

        when(iUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(iUserRepository.save(user)).thenReturn(user);

        User response = userService.updateRole(newRole, 1L);

        assertEquals(newRole, response.getRole());
    }

    @Test
    void updateUsername() {
        ERole role = ERole.USER;

        User user = new User();
        user.setId(1L);
        user.setUsername("Angel");
        user.setPassword("1234");
        user.setEmail("angel@gmail.com");
        user.setRole(role);

        when(iUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(iUserRepository.save(user)).thenReturn(user);

        User response = userService.updateUsername("Maksym", 1L);

        assertEquals("Maksym", response.getUsername());
    }

    @Test
    void getUserByUsername() {
        String username = "Angel";
        ERole role = ERole.USER;

        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("1234");
        user.setEmail("angel@gmail.com");
        user.setRole(role);

        when(iUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User response = userService.getUserByUsername(username);

        assertEquals(username, response.getUsername());
    }
}