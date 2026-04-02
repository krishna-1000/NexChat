package com.nexchat.NexChat.controller;

import com.nexchat.NexChat.modal.dto.request.websocket.ChatMessageRequest;
import com.nexchat.NexChat.modal.dto.request.websocket.ChatMessageResponse;
import com.nexchat.NexChat.service.ChatService;
import com.nexchat.NexChat.service.SecurityService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController{

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SecurityService securityService;

    public ChatController(ChatService chatService, SimpMessagingTemplate simpMessagingTemplate, SecurityService securityService){
        this.chatService = chatService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.securityService = securityService;
    }

    @MessageMapping("/chat.send")
    public void send(ChatMessageRequest request){
        System.out.println(" <<<<<<<<<<>>>>>>>>>>>>>>> "+ request.getContent() );
        Long senderId = securityService.getCurrentUserId();
        ChatMessageResponse chatMessageResponse = chatService.sendMessage(request.getChatRoomId(),request.getContent(),senderId);

        simpMessagingTemplate.convertAndSend("/topic/room."+request.getChatRoomId(),chatMessageResponse);

    }
}