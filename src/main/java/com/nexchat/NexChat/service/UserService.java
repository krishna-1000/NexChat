package com.nexchat.NexChat.service;

import com.nexchat.NexChat.mapper.GroupMapper;
import com.nexchat.NexChat.mapper.UserMapper;
import com.nexchat.NexChat.modal.dto.response.GroupResponse;
import com.nexchat.NexChat.modal.dto.response.UserResponse;
import com.nexchat.NexChat.modal.entity.ChatRoom;
import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.ChatRoomMemberRepository;
import com.nexchat.NexChat.repository.ChatRoomRepository;
import com.nexchat.NexChat.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ChatRoomRepository chatRoomRepository;
    private  final GroupMapper groupMapper;
    private  final ChatRoomMemberRepository chatRoomMemberRepository;

    public  UserService (UserMapper userMapper, UserRepository userRepository, ChatRoomRepository chatRoomRepository, GroupMapper groupMapper, ChatRoomMemberRepository chatRoomMemberRepository){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.chatRoomRepository = chatRoomRepository;
        this.groupMapper = groupMapper;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
    }

    public List<UserResponse> getAllUsers(){

        List<User> user = userRepository.findAll();

        return  userMapper.toResponseList(user);

    }

    public List<GroupResponse> getAllGroups(Long userId) {
        try {
            List<ChatRoom> groups = chatRoomMemberRepository.findAllGroupsForUser(userId);
            System.out.println(groups);


            return groupMapper.toResponseList(groups);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
