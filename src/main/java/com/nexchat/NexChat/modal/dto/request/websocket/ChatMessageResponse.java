package com.nexchat.NexChat.modal.dto.request.websocket;

import com.nexchat.NexChat.modal.entity.ChatRoom;
import com.nexchat.NexChat.modal.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {
    private Long id;

    private  String content;
    private String messageType;
    private String fileName;
    private LocalDateTime sentAt = LocalDateTime.now();

    private Long senderId;
}
