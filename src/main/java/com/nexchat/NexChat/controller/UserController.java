package com.nexchat.NexChat.controller;

import com.nexchat.NexChat.exception.InvalidActionException;
import com.nexchat.NexChat.modal.dto.request.UserUpdateRequest;
import com.nexchat.NexChat.modal.dto.response.ChatRoomResponse;
import com.nexchat.NexChat.modal.dto.request.GroupRequest;
import com.nexchat.NexChat.modal.dto.response.GroupResponse;
import com.nexchat.NexChat.modal.dto.response.UserResponse;
import com.nexchat.NexChat.service.ChatService;
import com.nexchat.NexChat.service.SecurityService;
import com.nexchat.NexChat.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;
    private final ChatService chatService;


    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/groups/{userId}")
    public ResponseEntity<List<GroupResponse>> getAllGroups(@PathVariable("userId") Long userId) {

        return ResponseEntity.ok(userService.getGroups(userId));
    }


    @GetMapping("/chat/private/{id}")
    public ResponseEntity<ChatRoomResponse> getPrivateChatRoom(@PathVariable("id") Long userB) {
        Long userA = securityService.getCurrentUserId();
        if (userB == null || userA == null) {
            throw new InvalidActionException("user is not valid");
        }
        return ResponseEntity.ok(chatService.getCommonRoom(userA, userB));

    }

    @PostMapping("/groups")
    public ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody GroupRequest groupRequest) {

        return ResponseEntity.ok(chatService.createGroup(groupRequest));

    }

    @GetMapping("/room/{id}")
    public ResponseEntity<ChatRoomResponse> getGroup(@PathVariable("id") Long groupId) {
        if(groupId == null){
            throw new InvalidActionException("Group id is invalid");
        }
        return ResponseEntity.ok(chatService.getGroup(groupId));

    }

    @DeleteMapping("/room/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable("id") Long groupId) {

        if(groupId == null){
            throw new InvalidActionException("Group id is Invalid");
        }
        chatService.deleteGroup(groupId);
        return ResponseEntity.ok("Group delete successfully!");

    }

    @DeleteMapping("room/{groupId}/{memberId}")
    public String exitFromGroup(@PathVariable("groupId") Long groupId, @PathVariable("memberId") Long memberId) {
        if(groupId == null || memberId == null){
            throw  new InvalidActionException("Group or Member is Not valid");
        }
        return chatService.deleteMemberFromGroup(memberId, groupId);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUserAccount(@PathVariable("userId") Long userId) {
        try {
            Long loginUser = securityService.getCurrentUserId();

            if (!Objects.equals(userId, loginUser)) {
                return ResponseEntity.badRequest().body("You can not delete other user account!");
            }

            String res = userService.deleteUserAccount(userId);

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    @PutMapping("/user")
//    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequest request) {
//        try {
//
//            Long loginUserId = securityService.getCurrentUserId();
//
//            if (!Objects.equals(request.getId(), loginUserId)) {
//                return ResponseEntity.badRequest().build();
//            }
//
//            UserResponse res = userService.updateUser(request);
//
//            return ResponseEntity.ok(res);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }


}
