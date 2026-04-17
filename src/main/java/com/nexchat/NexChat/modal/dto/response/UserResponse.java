package com.nexchat.NexChat.modal.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Date createdAt;
    private Long id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private boolean isOnline;
    private String role;
    private Date lastSeen;
}
