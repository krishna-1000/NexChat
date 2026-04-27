package com.nexchat.NexChat.modal.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResponse {
    private Long id;

    private String name;
    private boolean isprivate;
    private Long createdAt;
    private String createdBy;
    private List<ChatMessageResponse> messages;
}
