package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Wallet;
import com.flightDB.DBApp.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @GetMapping(path = "/{id}")
    public Wallet getById(@PathVariable Long id) {
        return walletService.getById(id);
    }
    @PostMapping(path = "/create/{id}")
    public Wallet createWallet(@PathVariable Long id) {
        return walletService.createWallet(id);
    }
    @GetMapping(path = "/user/{userId}")
    public Wallet getByUserId(@PathVariable Long userId) {
        return walletService.getByUserId(userId);
    }

    @PutMapping(path = "/user/addMoney/{userId}")
    public Wallet addMoney(@PathVariable Long userId,@RequestParam double money) {
        return walletService.addMoney(userId, money);
    }
}
