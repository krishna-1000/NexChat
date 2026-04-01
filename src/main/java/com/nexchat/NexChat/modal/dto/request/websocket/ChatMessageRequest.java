package com.nexchat.NexChat.modal.dto.request.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
   private Long chatRoomId;
    private String content;
}
