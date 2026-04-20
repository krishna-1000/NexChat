package com.nexchat.NexChat.modal.dto.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRequest {

    private  String adminName;
    private  String groupName;
    private List<Long> members = new ArrayList<>();
}
