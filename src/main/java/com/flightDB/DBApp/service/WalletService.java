package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Wallet;
import com.flightDB.DBApp.repository.IWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    IWalletRepository iWalletRepository;

    public Wallet getById(Long id) {
        return iWalletRepository.findById(id).orElseThrow();
    }

    public Wallet createWallet(Long id) {
        Wallet wallet = new Wallet();
        wallet.setId(id);
        return iWalletRepository.save(wallet);
    }
    public Wallet addEuro(Long id, double euro) {
        Wallet wallet = iWalletRepository.findById(id).orElseThrow();
        double sum = wallet.getEuro() + euro;
        wallet.setEuro(sum);
        return iWalletRepository.save(wallet);
    }
}
