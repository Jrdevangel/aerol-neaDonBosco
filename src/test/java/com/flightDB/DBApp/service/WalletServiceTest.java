package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.model.Wallet;
import com.flightDB.DBApp.repository.IWalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    @Mock
    private IWalletRepository iWalletRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_ShouldReturnWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setEuro(100.0);

        when(iWalletRepository.findById(1L)).thenReturn(Optional.of(wallet));

        Wallet result = walletService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100.0, result.getEuro());
        verify(iWalletRepository, times(1)).findById(1L);
    }

    @Test
    void createWallet_ShouldReturnNewWallet() {
        User user = new User();
        user.setId(1L);

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setEuro(0.0);
        wallet.setUser(user);

        when(userService.getUserById(1L)).thenReturn(user);
        when(iWalletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet result = walletService.createWallet(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(0.0, result.getEuro());
        assertEquals(user, result.getUser());
        verify(userService, times(1)).getUserById(1L);
        verify(iWalletRepository, times(1)).save(any(Wallet.class));
    }


    @Test
    void getByUserId_ShouldReturnWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setEuro(100.0);

        when(iWalletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));

        Wallet result = walletService.getByUserId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100.0, result.getEuro());
        verify(iWalletRepository, times(1)).findByUserId(1L);
    }

    @Test
    void addMoney_ShouldReturnUpdatedWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setEuro(100.0);

        when(iWalletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));
        when(iWalletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet result = walletService.addMoney(1L, 50.0);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(150.0, result.getEuro());
        verify(iWalletRepository, times(1)).findByUserId(1L);
        verify(iWalletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void saveWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setEuro(200);

        when(iWalletRepository.save(wallet)).thenReturn(wallet);

        walletService.saveWallet(wallet);

        verify(iWalletRepository, times(1)).save(wallet);
    }
}