package com.nexchat.NexChat.mapper;


import com.nexchat.NexChat.modal.dto.response.ChatRoomMemberResponse;
import com.nexchat.NexChat.modal.entity.ChatRoomMember;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = {UserMapper.class})
public interface ChatRoomMemberMapper {

    ChatRoomMemberResponse toResponse(ChatRoomMember chatRoomMember);

    List<ChatRoomMemberResponse> toResponseList(List<ChatRoomMember> chatRoomMemberList);
}
