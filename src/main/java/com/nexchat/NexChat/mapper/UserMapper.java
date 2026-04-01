package com.nexchat.NexChat.mapper;

import com.nexchat.NexChat.modal.dto.response.UserResponse;
import com.nexchat.NexChat.modal.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    List<UserResponse> toResponseList(List<User> user);
}
