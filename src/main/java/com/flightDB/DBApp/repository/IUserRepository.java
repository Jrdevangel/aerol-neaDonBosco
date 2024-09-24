package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
