package com.nexchat.NexChat.modal.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRequest {

    private  String adminName;
    private  String groupName;
}
