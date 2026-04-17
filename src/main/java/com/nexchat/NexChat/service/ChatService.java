package com.nexchat.NexChat.service;

import com.nexchat.NexChat.mapper.ChatRoomMapper;
import com.nexchat.NexChat.mapper.GroupMapper;
import com.nexchat.NexChat.mapper.MessageMapper;
import com.nexchat.NexChat.modal.dto.request.ChatRoomResponse;
import com.nexchat.NexChat.modal.dto.request.GroupRequest;
import com.nexchat.NexChat.modal.dto.request.websocket.ChatMessageResponse;
import com.nexchat.NexChat.modal.dto.response.GroupResponse;
import com.nexchat.NexChat.modal.entity.ChatRoom;
import com.nexchat.NexChat.modal.entity.ChatRoomMember;
import com.nexchat.NexChat.modal.entity.Message;
import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.ChatRoomMemberRepository;
import com.nexchat.NexChat.repository.ChatRoomRepository;
import com.nexchat.NexChat.repository.MessageRepository;
import com.nexchat.NexChat.repository.UserRepository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ChatService {

    private final UserRepository userRepository;
    private  final ChatRoomMapper chatRoomMapper;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final GroupMapper groupMapper;

    public ChatService(UserRepository userRepository, ChatRoomMapper chatRoomMapper, ChatRoomMemberRepository chatRoomMemberRepository, ChatRoomRepository chatRoomRepository, MessageRepository messageRepository, MessageMapper messageMapper, GroupMapper groupMapper) {
        this.userRepository = userRepository;
        this.chatRoomMapper = chatRoomMapper;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.groupMapper = groupMapper;
    }


    public ChatRoomResponse getCommonRoom(Long userA,Long userB) {

        Long chatRoomId =  chatRoomMemberRepository.findPrivateChatRoomId(userA, userB).orElseGet(() -> createChatRoom(userA, userB));

        ChatRoom chatRoom =  chatRoomRepository.findById(chatRoomId).orElseThrow(()->new UsernameNotFoundException("Chat room not exist"));
        return  chatRoomMapper.toResponse(chatRoom);

    }

    public Long createChatRoom(Long userA, Long userB) {
        User user1 = userRepository.findById(userA).orElseThrow(() -> new UsernameNotFoundException("UserA Not Found"));
        User user2 = userRepository.findById(userB).orElseThrow(() -> new UsernameNotFoundException("UserB Not Found"));

        ChatRoom chatRoom1 = new ChatRoom();
        chatRoom1.setCreatedBy(user1.getUsername());
        chatRoom1.setName("User" + userA + "_" + "user" + userB + "_" + "room");
        chatRoom1.setIsprivate(true);
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom1);

        ChatRoomMember member1 = new ChatRoomMember();
        member1.setChatRoom(savedRoom);
        member1.setUser(user1);

        ChatRoomMember member2 = new ChatRoomMember();
        member2.setChatRoom(savedRoom);
        member2.setUser(user2);

        chatRoomMemberRepository.save(member1);
        chatRoomMemberRepository.save(member2);

        return savedRoom.getId();


    }

    public ChatMessageResponse sendMessage(Long chatroomId,String content,String username){
        try {
                ChatRoom chatRoom = chatRoomRepository.findById(chatroomId).orElseThrow(()->new UsernameNotFoundException("Chat room not exist"));
                User sender = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Sender Not Found"));

            Message message = new Message();
            message.setContent(content);
            message.setSentAt(LocalDateTime.now());
            message.setMessageType("TEXT");
            message.setChatRoom(chatRoom);
            message.setSender(sender);
            Message savedmessage = messageRepository.save(message);
            return messageMapper.toResponse(savedmessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GroupResponse createGroup(GroupRequest groupRequest){


        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setIsprivate(false);
        chatRoom.setCreatedBy(groupRequest.getAdminName());
        chatRoom.setName(groupRequest.getGroupName());


        ChatRoom savedGroup = chatRoomRepository.save(chatRoom);
        return groupMapper.toResponse(savedGroup);
    }
}
