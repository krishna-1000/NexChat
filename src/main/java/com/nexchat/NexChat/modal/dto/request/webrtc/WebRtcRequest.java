package com.nexchat.NexChat.modal.dto.request.webrtc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class  WebRtcRequest {
    private String sender;
    private String targetUser;
    private String type;
    private Object data;
    private String callType;
}
