package com.nexchat.NexChat.mapper;

import com.nexchat.NexChat.modal.dto.response.ChatMessageResponse;
import com.nexchat.NexChat.modal.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "sender.id",target = "senderId")
    @Mapping(source = "sender.username",target = "senderName")
    ChatMessageResponse toResponse(Message message);

    List<ChatMessageResponse> toResponseList(List<Message> message);

}
