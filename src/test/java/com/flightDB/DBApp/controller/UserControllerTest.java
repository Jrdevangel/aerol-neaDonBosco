package com.flightDB.DBApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightDB.DBApp.model.ERole;
import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.model.Wallet;
import com.flightDB.DBApp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        User updatedUser = new User(1L, "UpdatedUsername", "password", "email@test.com", ERole.USER, new HashSet<>(), new Wallet());
        when(userService.updateUsername("UpdatedUsername", 1L)).thenReturn(updatedUser);

        mockMvc.perform(put("/api/user/updateUsername/{id}", 1L)
                        .param("username", "UpdatedUsername")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("UpdatedUsername"))
                .andExpect(jsonPath("$.email").value("email@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userService, times(1)).updateUsername("UpdatedUsername", 1L);
    }

    @Test
    void getUserProfile_ShouldReturnUser_WhenUserExists() throws Exception {
        User user = new User(1L, "testUser", "password", "email@test.com", ERole.USER, new HashSet<>(), new Wallet());
        when(authentication.getName()).thenReturn("testUser");
        when(userService.getUserByUsername("testUser")).thenReturn(user);

        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("email@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userService, times(1)).getUserByUsername("testUser");
    }

    @Test
    void getUserProfile_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(authentication.getName()).thenReturn("nonexistentUser");
        when(userService.getUserByUsername("nonexistentUser")).thenReturn(null);

        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserByUsername("nonexistentUser");
    }

    @Test
    void getUsername_ShouldReturnUser() throws Exception {
        User user = new User(1L, "testUser", "password", "email@test.com", ERole.USER, new HashSet<>(), new Wallet());
        when(userService.getUserByUsername("testUser")).thenReturn(user);

        mockMvc.perform(get("/api/user/getUsername")
                        .param("username", "testUser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("email@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userService, times(1)).getUserByUsername("testUser");
    }

    @Test
    void updatePassword_ShouldReturnUpdatedUser() throws Exception {
        User user = new User(1L, "testUser", "newPassword", "email@test.com", ERole.USER, new HashSet<>(), new Wallet());
        when(userService.updatePassword("oldPassword", "newPassword", 1L)).thenReturn(user);

        mockMvc.perform(put("/api/user/updatePassword/{id}", 1L)
                        .param("oldPassword", "oldPassword")
                        .param("password", "newPassword")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("email@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userService, times(1)).updatePassword("oldPassword", "newPassword", 1L);
    }

    @Test
    void getById_ShouldReturnUser() throws Exception {
        User user = new User(1L, "testUser", "password", "email@test.com", ERole.USER, new HashSet<>(), new Wallet());

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/user/get/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("email@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userService, times(1)).getUserById(1L);
    }


    @Test
    void updateRole_ShouldReturnUpdatedUserRole() throws Exception {
        User user = new User(1L, "testUser", "password", "email@test.com", ERole.ADMIN, new HashSet<>(), new Wallet());
        when(userService.updateRole(ERole.ADMIN, 1L)).thenReturn(user);

        mockMvc.perform(put("/api/user/updateRole/{id}", 1L)
                        .param("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("email@test.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(userService, times(1)).updateRole(ERole.ADMIN, 1L);
    }

    @Test
    void updateRole_ShouldReturnBadRequest_WhenRoleIsInvalid() throws Exception {
        mockMvc.perform(put("/api/user/updateRole/{id}", 1L)
                        .param("role", "INVALID_ROLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateRole(any(ERole.class), anyLong());
    }

    @Test
    void deleteUser() throws Exception{
        Long userId = 1L;

        mockMvc.perform(delete("/api/user/delete/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(userId);
    }
}