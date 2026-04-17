package com.nexchat.NexChat.controller;

import com.nexchat.NexChat.modal.dto.request.webrtc.WebRtcRequest;
import com.nexchat.NexChat.modal.dto.request.websocket.ChatMessageRequest;
import com.nexchat.NexChat.modal.dto.request.websocket.ChatMessageResponse;
import com.nexchat.NexChat.service.ChatService;
import com.nexchat.NexChat.service.SecurityService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SecurityService securityService;

    public ChatController(ChatService chatService, SimpMessagingTemplate simpMessagingTemplate, SecurityService securityService) {
        this.chatService = chatService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.securityService = securityService;
    }

    @MessageMapping("/chat.send")
    public void send(ChatMessageRequest request, Principal principal) {
        try {


            System.out.println(" <<<<<<<<<<>>>>>>>>>>>>>>> " + request.getContent());
            String username = principal.getName();
            System.out.println("PRIcinPal -____----" + username);
            ChatMessageResponse chatMessageResponse = chatService.sendMessage(request.getChatRoomId(), request.getContent(), username);

            simpMessagingTemplate.convertAndSend("/topic/room." + request.getChatRoomId(), chatMessageResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping("/webrtc-signal")
    public void handleWebRtcSignal(@Payload WebRtcRequest signal) {

        try {
            simpMessagingTemplate.convertAndSendToUser(signal.getTargetUser(), "/queue/webrtc", signal);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}