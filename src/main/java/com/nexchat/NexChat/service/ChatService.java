package com.nexchat.NexChat.service;

import com.nexchat.NexChat.exception.ResourceNotFoundException;
import com.nexchat.NexChat.exception.UnauthorizedException;
import com.nexchat.NexChat.mapper.ChatRoomMapper;
import com.nexchat.NexChat.mapper.GroupMapper;
import com.nexchat.NexChat.mapper.MessageMapper;
import com.nexchat.NexChat.modal.dto.response.ChatRoomResponse;
import com.nexchat.NexChat.modal.dto.request.GroupRequest;
import com.nexchat.NexChat.modal.dto.request.websocket.ChatMessageRequest;
import com.nexchat.NexChat.modal.dto.response.ChatMessageResponse;
import com.nexchat.NexChat.modal.dto.response.GroupResponse;
import com.nexchat.NexChat.modal.entity.ChatRoom;
import com.nexchat.NexChat.modal.entity.ChatRoomMember;
import com.nexchat.NexChat.modal.entity.Message;
import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.ChatRoomMemberRepository;
import com.nexchat.NexChat.repository.ChatRoomRepository;
import com.nexchat.NexChat.repository.MessageRepository;
import com.nexchat.NexChat.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final GroupMapper groupMapper;


    public ChatRoomResponse getCommonRoom(Long userA, Long userB) {

        Long chatRoomId = chatRoomMemberRepository.findPrivateChatRoomId(userA, userB).orElseGet(() -> createChatRoom(userA, userB));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new ResourceNotFoundException("Chat room not exist"));
        return chatRoomMapper.toResponse(chatRoom);

    }

    @Transactional
    public Long createChatRoom(Long userA, Long userB) {
        User user1 = userRepository.findById(userA).orElseThrow(() -> new ResourceNotFoundException("username " + userA + " Not Found"));
        User user2 = userRepository.findById(userB).orElseThrow(() -> new ResourceNotFoundException("username " + userB + " Not Found"));

        ChatRoom chatRoom1 = new ChatRoom();
        chatRoom1.setCreatedBy(user1.getUsername());
        chatRoom1.setName(user1.getUsername() + "_" + "and" + user2.getUsername() + "_" + "room");
        chatRoom1.setPrivateRoom(true);
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom1);

        saveMember(savedRoom,user1);
        saveMember(savedRoom,user2);

        return savedRoom.getId();


    }

    @Transactional
    public ChatMessageResponse sendMessage(ChatMessageRequest request, String username) {

        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId()).orElseThrow(() -> new ResourceNotFoundException("Chat room not found"));
        User sender = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));


        if (!chatRoomMemberRepository.existsByUserAndChatRoom(sender, chatRoom)) {
            throw new UnauthorizedException("You are not a member of this room");
        }
        Message message = new Message();
        message.setContent(request.getContent());
        message.setSentAt(LocalDateTime.now());
        message.setMessageType(request.getType());
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        Message savedmessage = messageRepository.save(message);
        return messageMapper.toResponse(savedmessage);


    }

    @Transactional
    public GroupResponse createGroup(GroupRequest groupRequest) {

        User admin = userRepository.findByUsername(groupRequest.getAdminName()).orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setPrivateRoom(false);
        chatRoom.setCreatedBy(groupRequest.getAdminName());
        chatRoom.setName(groupRequest.getGroupName());
        ChatRoom savedGroup = chatRoomRepository.save(chatRoom);


       saveMember(savedGroup,admin);

        if (groupRequest.getMembers() != null) {

            for (Long userId : groupRequest.getMembers()) {
                User member = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Member Id " + userId + " Not Found"));
                saveMember(savedGroup,member);
            }
        }
        return groupMapper.toResponse(savedGroup);
    }

    public ChatRoomResponse getGroup(Long groupId) {

        ChatRoom chatRoom = chatRoomRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group Not found"));

        return chatRoomMapper.toResponse(chatRoom);
    }

    @Transactional
    public void deleteGroup(Long groupId) {

        if (!chatRoomRepository.existsById(groupId)) {
            throw new ResourceNotFoundException("Group not found with ID: " + groupId);
        }
        chatRoomRepository.deleteById(groupId);

    }

    public String deleteMemberFromGroup(Long memberId, Long groupId) {

            boolean isUser = userRepository.existsById(memberId);
            if(!isUser){
                throw new ResourceNotFoundException("Member is not exist in Group");
            }
            chatRoomMemberRepository.exitMember(memberId, groupId);
            return "Exit successfully from group";




    }

    private void saveMember(ChatRoom room, User user) {
        ChatRoomMember member = new ChatRoomMember();
        member.setUser(user);
        member.setChatRoom(room);
        chatRoomMemberRepository.save(member);
    }
}
