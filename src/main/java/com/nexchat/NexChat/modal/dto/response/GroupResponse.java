package com.nexchat.NexChat.modal.dto.response;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupResponse {

    private Long groupId;
    private String groupName;


}
