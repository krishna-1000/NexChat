package com.nexchat.NexChat.modal.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private final Date createdAt = new Date();
    private Long id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private boolean isOnline = false;
    private String role;
    private Date lastSeen;
}
