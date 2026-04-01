package com.nexchat.NexChat.controller;

import com.nexchat.NexChat.modal.dto.request.websocket.ChatMessageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController{

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void send(ChatMessageRequest request){
        System.out.println(" <<<<<<<<<<>>>>>>>>>>>>>>> "+ request.getContent() );

        simpMessagingTemplate.convertAndSend("/topic/public",request.getContent());

    }
}