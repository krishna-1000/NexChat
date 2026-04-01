package com.nexchat.NexChat.service;

import com.nexchat.NexChat.mapper.UserMapper;
import com.nexchat.NexChat.modal.dto.response.UserResponse;
import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public  UserService (UserMapper userMapper,UserRepository userRepository){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponse> getAllUsers(){

        List<User> user = userRepository.findAll();

        return  userMapper.toResponseList(user);

    }

}
