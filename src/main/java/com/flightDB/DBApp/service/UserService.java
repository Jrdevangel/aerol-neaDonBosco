package com.flightDB.DBApp.service;

import com.flightDB.DBApp.controller.UserController;
import com.flightDB.DBApp.model.ERole;
import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Optional;

@Service

public class UserService {

    //hacer test
    @Autowired
    IUserRepository iUserRepository;

    //hacer test
    public void deleteUser(Long Id){
        iUserRepository.deleteById(Id);
    }

    //hacer test
    public User updateUsername(String username, Long id){
        Optional<User> user = iUserRepository.findById(id);
        user.get().setUsername(username);
        return iUserRepository.save(user.get());
    }

    //hacer test
    public User getUserByUsername(String username){
        return iUserRepository.findByUsername(username).orElseThrow();
    }

    public User updatePassword(String oldPassword, String newPassword, Long id) {
        User user = iUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedNewPassword);

        return iUserRepository.save(user);
    }

    public User getUserById(Long id){
        return iUserRepository.findById(id).orElseThrow();
    }

    public User updateRole(ERole role, Long id){
        User response = iUserRepository.findById(id).orElseThrow();
        response.setRole(role);
        return iUserRepository.save(response);
    }
}