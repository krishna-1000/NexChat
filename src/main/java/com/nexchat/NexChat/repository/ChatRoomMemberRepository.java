package com.nexchat.NexChat.repository;

import com.nexchat.NexChat.modal.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember,Long> {
}
