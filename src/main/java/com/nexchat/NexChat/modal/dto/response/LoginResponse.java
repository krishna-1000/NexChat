package com.nexchat.NexChat.modal.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private String username;
    private String token;

}
