package com.nexchat.NexChat.modal.dto.request.websocket;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
    private Long chatRoomId;
    private String type;
    private String content;
}
