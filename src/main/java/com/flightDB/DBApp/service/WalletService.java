package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.model.Wallet;
import com.flightDB.DBApp.repository.IWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    IWalletRepository iWalletRepository;

    @Autowired
    UserService userService;

    public Wallet getById(Long id) {
        return iWalletRepository.findById(id).orElseThrow();
    }

    public Wallet createWallet(Long id) {
        User response = userService.getUserById(id);
        Wallet wallet = new Wallet();
        wallet.setEuro(0);
        wallet.setUser(response);
        return iWalletRepository.save(wallet);
    }
    public Wallet getByUserId(Long userId) {
        return iWalletRepository.findByUserId(userId).orElseThrow();
    }

    public Wallet addMoney(Long userId, double money) {
        Wallet wallet = iWalletRepository.findByUserId(userId).orElseThrow();
        double sum = wallet.getEuro() + money;
        wallet.setEuro(sum);
        return iWalletRepository.save(wallet);
    }

    public void saveWallet(Wallet wallet) {
        iWalletRepository.save(wallet);
    }
}
