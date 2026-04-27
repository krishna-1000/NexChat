package com.nexchat.NexChat.controller;

import com.nexchat.NexChat.exception.InvalidActionException;
import com.nexchat.NexChat.modal.dto.request.webrtc.WebRtcRequest;
import com.nexchat.NexChat.modal.dto.request.websocket.ChatMessageRequest;
import com.nexchat.NexChat.modal.dto.response.ChatMessageResponse;
import com.nexchat.NexChat.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;



    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageRequest request, Principal principal) {

            String username = principal.getName();
            ChatMessageResponse chatMessageResponse = chatService.sendMessage(request, username);

            simpMessagingTemplate.convertAndSend("/topic/room." + request.getChatRoomId(), chatMessageResponse);
    }

    @MessageMapping("/webrtc-signal")
    public void handleWebRtcSignal(@Payload WebRtcRequest signal) {


        if(signal == null){
            throw  new InvalidActionException("Request is not valid");
        }

        simpMessagingTemplate.convertAndSendToUser(signal.getTargetUser(), "/queue/webrtc", signal);


    }
}