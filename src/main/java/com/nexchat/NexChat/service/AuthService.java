package com.nexchat.NexChat.service;

import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String signUp(User user) {

        try {
            if (user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getEmail().isEmpty()) {
                return "Username or password is empty";
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "Signup Successfully";

        } catch (Exception e) {
            return e.toString();
        }
    }

}
