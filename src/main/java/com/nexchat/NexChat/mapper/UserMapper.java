package com.nexchat.NexChat.mapper;

import com.nexchat.NexChat.modal.dto.response.UserResponse;
import com.nexchat.NexChat.modal.entity.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Set<UserResponse> toResponseSet(Set<User> user);
    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> user);

}
