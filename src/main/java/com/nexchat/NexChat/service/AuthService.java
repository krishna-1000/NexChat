package com.nexchat.NexChat.service;

import com.nexchat.NexChat.mapper.SignupMapper;
import com.nexchat.NexChat.modal.dto.request.authrequest.SignupRequest;
import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final UserRepository userRepository;
    private final SignupMapper signupMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, SignupMapper signupMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.signupMapper = signupMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public String signUp(SignupRequest signupRequest) {

        try {
            if (signupRequest.getUsername().isEmpty() || signupRequest.getPassword().isEmpty() || signupRequest.getEmail().isEmpty()) {
                return "Username or password is empty";
            }
            signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            User user = signupMapper.toEntity(signupRequest);
            userRepository.save(user);
            return "Signup Successfully";

        } catch (Exception e) {
            return e.toString();
        }
    }

}
