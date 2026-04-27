package com.nexchat.NexChat.service;

import com.nexchat.NexChat.exception.InvalidActionException;
import com.nexchat.NexChat.exception.UserAlreadyExistsException;
import com.nexchat.NexChat.mapper.SignupMapper;
import com.nexchat.NexChat.modal.dto.request.authrequest.SignupRequest;
import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void signUp(SignupRequest signupRequest) {

            if (signupRequest.getUsername().isBlank() ||
                    signupRequest.getPassword().isBlank() ||
                    signupRequest.getEmail().isBlank() ) {
               throw new InvalidActionException("Required field are missing");
            }
            boolean isUserExist = userRepository.existsByEmail(signupRequest.getEmail());
            boolean isUserNameExist = userRepository.existsByUsername(signupRequest.getUsername());

            if(isUserExist){
                throw new UserAlreadyExistsException("Email is already registered");
            }
            if(isUserNameExist){
                throw new UserAlreadyExistsException("Username is already taken");
            }
            signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            User user = signupMapper.toEntity(signupRequest);
            userRepository.save(user);



    }

}
