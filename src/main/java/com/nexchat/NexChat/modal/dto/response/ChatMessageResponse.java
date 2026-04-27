package com.nexchat.NexChat.modal.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long id;

    private  String content;
    private String messageType;
    private String fileName;
    private LocalDateTime sentAt;
    private Long senderId;
    private String senderName;
}
