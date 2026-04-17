package com.nexchat.NexChat.mapper;

import com.nexchat.NexChat.modal.dto.request.authrequest.SignupRequest;
import com.nexchat.NexChat.modal.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignupMapper {

    SignupRequest toResponse(User user);
    User toEntity(SignupRequest signupRequest);
}
