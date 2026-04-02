package com.nexchat.NexChat.controller;

import com.nexchat.NexChat.modal.dto.response.UserResponse;
import com.nexchat.NexChat.service.ChatService;
import com.nexchat.NexChat.service.SecurityService;
import com.nexchat.NexChat.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;
    private final ChatService chatService;

    public UserController(UserService userService, SecurityService securityService, ChatService chatService) {
        this.userService = userService;

        this.securityService = securityService;
        this.chatService = chatService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponses = userService.getAllUsers();
        if (userResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/room/private/{id}")
    public ResponseEntity<Long> getPrivateChatRoomId(@PathVariable("id") Long userB) {
        Long userA = securityService.getCurrentUserId();
        Long chatroomId = chatService.getCommonRoomId(userA, userB);
        return ResponseEntity.ok(chatroomId);

    }
}
