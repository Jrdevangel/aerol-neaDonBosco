package com.flightDB.DBApp.service;

import com.flightDB.DBApp.controller.UserController;
import com.flightDB.DBApp.model.ERole;
import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Optional;

@Service

public class UserService {

    @Autowired
    IUserRepository iUserRepository;

    public void deleteUser(Long Id){
        iUserRepository.deleteById(Id);
    }

    public User updateUsername(String username, Long id){
        Optional<User> user = iUserRepository.findById(id);
        user.get().setUsername(username);
        return iUserRepository.save(user.get());
    }

    public User getUserByUsername(String username){
        return iUserRepository.findByUsername(username).orElseThrow();
    }

    public User updatePassword(String password, Long id){
        User response = iUserRepository.findById(id).orElseThrow();
        response.setPassword(password);
        return iUserRepository.save(response);
    }

    public User getUserById(Long id){
        return iUserRepository.findById(id).orElseThrow();
    }

    public User updateRole(ERole username, Long id){
        User response = iUserRepository.findById(id).orElseThrow();
        response.setRole(username);
        return iUserRepository.save(response);
    }
}