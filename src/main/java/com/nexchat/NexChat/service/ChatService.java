package com.nexchat.NexChat.service;

import com.nexchat.NexChat.mapper.MessageMapper;
import com.nexchat.NexChat.modal.dto.request.websocket.ChatMessageResponse;
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

@Service
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public ChatService(UserRepository userRepository, ChatRoomMemberRepository chatRoomMemberRepository, ChatRoomRepository chatRoomRepository, MessageRepository messageRepository, MessageMapper messageMapper) {
        this.userRepository = userRepository;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }


    public Long getCommonRoomId(Long userA,Long userB) {

        return chatRoomMemberRepository.findPrivateChatRoomId(userA, userB).orElseGet(() -> createChatRoom(userA, userB));


    }

    public Long createChatRoom(Long userA, Long userB) {
        User user1 = userRepository.findById(userA).orElseThrow(() -> new UsernameNotFoundException("UserA Not Found"));
        User user2 = userRepository.findById(userB).orElseThrow(() -> new UsernameNotFoundException("UserB Not Found"));

        ChatRoom chatRoom1 = new ChatRoom();
        chatRoom1.setCreatedBy(userA);
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

    public ChatMessageResponse sendMessage(Long chatroomId,String content,Long senderId){
        try {
                ChatRoom chatRoom = chatRoomRepository.findById(chatroomId).orElseThrow(()->new UsernameNotFoundException("Chat room not exist"));
                User sender = userRepository.findById(senderId).orElseThrow(()->new UsernameNotFoundException("Sender Not Found"));

            Message message = new Message();
            message.setContent(content);
            message.setSentAt(LocalDateTime.now());
            message.setMessageType("TEXT");
            message.setChatRoom(chatRoom);
            message.setSender(sender);
            message.setFileName("text_message");
            Message savedmessage = messageRepository.save(message);


            return messageMapper.toResponse(savedmessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
