package com.nexchat.NexChat.modal.dto.request.authrequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
}
