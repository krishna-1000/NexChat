package com.nexchat.NexChat.controller;

import com.nexchat.NexChat.modal.dto.request.ChatRoomResponse;
import com.nexchat.NexChat.modal.dto.request.GroupRequest;
import com.nexchat.NexChat.modal.dto.response.GroupResponse;
import com.nexchat.NexChat.modal.dto.response.UserResponse;
import com.nexchat.NexChat.modal.entity.ChatRoom;
import com.nexchat.NexChat.service.ChatService;
import com.nexchat.NexChat.service.SecurityService;
import com.nexchat.NexChat.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
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
    public ResponseEntity<ChatRoomResponse> getPrivateChatRoom(@PathVariable("id") Long userB) {
        Long userA = securityService.getCurrentUserId();
        ChatRoomResponse chatroom = chatService.getCommonRoom(userA, userB);
        return ResponseEntity.ok(chatroom);

    }

    @PostMapping("/room/group")
    public ResponseEntity<GroupResponse> createGroup(@RequestBody  GroupRequest groupRequest) {
        System.out.println(groupRequest);
        if(groupRequest.getGroupName() == null){
            return  ResponseEntity.unprocessableContent().build();
        }
        GroupResponse groupResponse = chatService.createGroup(groupRequest);


        return ResponseEntity.ok(groupResponse);

    }

}
