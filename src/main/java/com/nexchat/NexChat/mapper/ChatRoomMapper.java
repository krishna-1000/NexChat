package com.nexchat.NexChat.mapper;

import com.nexchat.NexChat.modal.dto.response.ChatRoomResponse;
import com.nexchat.NexChat.modal.entity.ChatRoom;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring",uses = {MessageMapper.class})
public interface ChatRoomMapper {

    ChatRoomResponse toResponse(ChatRoom chatRoom);
}
