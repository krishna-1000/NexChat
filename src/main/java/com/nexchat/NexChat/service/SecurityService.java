package com.nexchat.NexChat.service;

import com.nexchat.NexChat.exception.UnauthorizedException;
import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User is not Longed in");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User ID not found in security context"));
        return user.getId();

    }
}