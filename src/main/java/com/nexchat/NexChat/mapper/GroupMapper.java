package com.nexchat.NexChat.mapper;

import com.nexchat.NexChat.modal.dto.response.GroupResponse;
import com.nexchat.NexChat.modal.entity.ChatRoom;
import com.nexchat.NexChat.modal.entity.ChatRoomMember;
import com.nexchat.NexChat.modal.entity.Message;
import jakarta.persistence.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(source = "name",target = "groupName")
    @Mapping(source = "id",target = "groupId")
    GroupResponse toResponse(ChatRoom chatRoom);


}
