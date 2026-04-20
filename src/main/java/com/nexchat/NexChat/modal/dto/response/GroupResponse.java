package com.nexchat.NexChat.modal.dto.response;


import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupResponse {

    private Long groupId;
    private String groupName;
    private String createdBy;
}
