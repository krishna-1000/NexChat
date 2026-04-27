package com.nexchat.NexChat.modal.dto.response;

import com.nexchat.NexChat.modal.entity.User;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomMemberResponse {

    private UserResponse user;
}
