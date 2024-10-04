package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IWalletRepository extends CrudRepository<Wallet ,Long> {
    Optional<Wallet> findByUserId(Long userId);
}
