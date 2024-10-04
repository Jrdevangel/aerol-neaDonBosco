package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.ERole;
import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.repository.IFlightRepository;
import com.flightDB.DBApp.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.management.relation.Role;
import java.util.Optional;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private MockMvc mockMvc;

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
    }

    @Test
    void updateUsername() {
    }

    @Test
    void getUserByUsername() {
    }


    @Test
    void updatePassword_ShouldUpdatePassword_WhenOldPasswordIsCorrect() {
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
        assertNotNull(updatedUser, "The updatedUser should not be null");
        assertTrue(realPasswordEncoder.matches(newPassword, updatedUser.getPassword()), "The new password should match the encoded password");
        verify(iUserRepository).save(mockUser);
    }

    @Test
    void updatePassword_ShouldThrowException_WhenOldPasswordIsIncorrect() {
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
    void updatePassword_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        when(iUserRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(oldPassword, newPassword, userId);
        });

        assertEquals("User not found", exception.getMessage());

        verify(iUserRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById() {
        ERole eRole = ERole.USER;
        User flight = new User();
        flight.setId(1L);
        flight.setUsername("Angel");
        flight.setPassword("1234");
        flight.setEmail("angel@gmail.com");
        flight.setRole(eRole);

        when(iUserRepository.findById(1L)).thenReturn(Optional.of(flight));
        User response = userService.getUserById(1L);
        assertEquals(1L, response.getId());
    }

    @Test
    void updateRole() {
        ERole eRole = ERole.USER;
        ERole newErole = ERole.ADMIN;
        User flight = new User();
        flight.setId(1L);
        flight.setUsername("Angel");
        flight.setPassword("1234");
        flight.setEmail("angel@gmail.com");
        flight.setRole(eRole);
        when(iUserRepository.findById(1L)).thenReturn(Optional.of(flight));

        when(iUserRepository.save(flight)).thenReturn(flight);
        User response = userService.updateRole(newErole, 1L);
        assertEquals(newErole, response.getRole());
    }
}