package com.nexchat.NexChat.service;

import com.nexchat.NexChat.modal.User;
import com.nexchat.NexChat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomeUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public  CustomeUserDetailsService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user Not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .authorities("USER")
                .password(user.getPassword()).build();
    }
}
