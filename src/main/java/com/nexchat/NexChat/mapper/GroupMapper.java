package com.nexchat.NexChat.mapper;

import com.nexchat.NexChat.modal.dto.response.GroupResponse;
import com.nexchat.NexChat.modal.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = {ChatRoomMemberMapper.class})
public interface GroupMapper {
    @Mapping(source = "name",target = "groupName")
    @Mapping(source = "id",target = "groupId")

    GroupResponse toResponse(ChatRoom chatRoom);


    List<GroupResponse> toResponseList(List<ChatRoom> chatRooms);


}
