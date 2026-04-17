package com.nexchat.NexChat.modal.dto.request;

import com.nexchat.NexChat.modal.dto.request.websocket.ChatMessageResponse;
import com.nexchat.NexChat.modal.dto.response.UserResponse;
import com.nexchat.NexChat.modal.entity.ChatRoomMember;
import com.nexchat.NexChat.modal.entity.Message;
import lombok.*;

import java.util.List;
import java.util.Set;

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
    private Long createdBy;
    private List<ChatMessageResponse> messages;
}
