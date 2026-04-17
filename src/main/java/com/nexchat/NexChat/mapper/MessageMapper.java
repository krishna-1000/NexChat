package com.nexchat.NexChat.mapper;

import com.nexchat.NexChat.modal.dto.request.websocket.ChatMessageResponse;
import com.nexchat.NexChat.modal.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "sender.id",target = "senderId")
    ChatMessageResponse toResponse(Message message);

    List<ChatMessageResponse> toResponseList(List<Message> message);

}
