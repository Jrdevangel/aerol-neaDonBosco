package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.model.Wallet;
import com.flightDB.DBApp.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    void getById_ShouldReturnWallet() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setEuro(100.0);

        when(walletService.getById(1L)).thenReturn(wallet);

        mockMvc.perform(get("/api/wallet/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.euro").value(100.0));

        verify(walletService, times(1)).getById(1L);
    }

    @Test
    void createWallet_ShouldReturnCreatedWallet() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setEuro(0.0);

        when(walletService.createWallet(1L)).thenReturn(wallet);

        mockMvc.perform(post("/api/wallet/create/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.euro").value(0.0));

        verify(walletService, times(1)).createWallet(1L);
    }

    @Test
    void getByUserId_ShouldReturnWallet() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setEuro(50.0);

        when(walletService.getByUserId(1L)).thenReturn(wallet);

        mockMvc.perform(get("/api/wallet/user/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.euro").value(50.0));

        verify(walletService, times(1)).getByUserId(1L);
    }

    @Test
    void addMoney_ShouldReturnUpdatedWallet() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setEuro(150.0);

        when(walletService.addMoney(1L, 50.0)).thenReturn(wallet);

        mockMvc.perform(put("/api/wallet/user/addMoney/{userId}", 1L)
                        .param("money", "50.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.euro").value(150.0));

        verify(walletService, times(1)).addMoney(1L, 50.0);
    }
}