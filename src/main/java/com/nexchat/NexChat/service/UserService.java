package com.nexchat.NexChat.service;

import com.nexchat.NexChat.exception.ResourceNotFoundException;
import com.nexchat.NexChat.mapper.GroupMapper;
import com.nexchat.NexChat.mapper.UserMapper;
import com.nexchat.NexChat.modal.dto.request.UserUpdateRequest;
import com.nexchat.NexChat.modal.dto.response.GroupResponse;
import com.nexchat.NexChat.modal.dto.response.UserResponse;
import com.nexchat.NexChat.modal.entity.ChatRoom;
import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.ChatRoomMemberRepository;
import com.nexchat.NexChat.repository.ChatRoomRepository;
import com.nexchat.NexChat.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ChatRoomRepository chatRoomRepository;
    private  final GroupMapper groupMapper;
    private  final ChatRoomMemberRepository chatRoomMemberRepository;



    public List<UserResponse> getUsers(){

        List<User> user = userRepository.findAll();
        return  userMapper.toResponseList(user);

    }

    public List<GroupResponse> getGroups(Long userId) {

            List<ChatRoom> groups = chatRoomMemberRepository.findAllGroupsForUser(userId);
           if(groups != null){
               return groupMapper.toResponseList(groups);
           }
           return null;


    }

    public String deleteUserAccount(Long userId) {
        try{
            if(userId != null){
                 userRepository.deleteById(userId);
                 return "Account Deleted Successfully!";
            }
            return "User Not Exist";
        } catch (Exception e) {
            throw  new RuntimeException("User Not Found");
        }
    }

//    @Transactional
//    public UserResponse updateUser(UserUpdateRequest request) {
//       int check = userRepository.updateUserById(request.getId(),request.getUsername());
//       if(check < 1){
//           throw  new ResourceNotFoundException("User not found to update");
//       }
//        User updatedUser = userRepository.findById(request.getId()).orElseThrow(()-> new ResourceNotFoundException("User Updated but not found"));
//
//        return userMapper.toResponse(updatedUser);
//    }
}
