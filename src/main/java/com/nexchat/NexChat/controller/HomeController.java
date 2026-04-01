package com.nexchat.NexChat.controller;

import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/private")
    public String privateRoot() {
        return "private path accessed";

    }
    @GetMapping("/")
    public String homePage(){
        return "home";
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepository.findAll();
    }
}
