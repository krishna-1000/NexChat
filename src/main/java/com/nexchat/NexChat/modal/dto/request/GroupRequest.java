package com.nexchat.NexChat.modal.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRequest {

    @NotBlank
    private  String adminName;

    @NotBlank
    private  String groupName;
    private List<Long> members = new ArrayList<>();
}
