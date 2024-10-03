package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface IWalletRepository extends CrudRepository<Wallet ,Long> {
}
