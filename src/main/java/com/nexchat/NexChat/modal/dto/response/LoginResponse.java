package com.nexchat.NexChat.modal.dto.response;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private String username;
    private String token;
    private String refreshToken;
    private Long id;
    private String email;
    private Date createdAt;
    private String bio;

}
