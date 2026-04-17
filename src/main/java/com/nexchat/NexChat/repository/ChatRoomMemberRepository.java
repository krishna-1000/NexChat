package com.nexchat.NexChat.repository;

import com.nexchat.NexChat.modal.entity.ChatRoom;
import com.nexchat.NexChat.modal.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    @Query("SELECT m.chatRoom.id FROM ChatRoomMember m " +
            "JOIN m.chatRoom c " +
            "WHERE m.user.id IN (:userA, :userB) " +
            "AND c.isprivate = true " +
            "GROUP BY m.chatRoom.id " +
            "HAVING COUNT(DISTINCT m.user.id) = 2")
    Optional<Long> findPrivateChatRoomId(@Param("userA") Long userA, @Param("userB") Long userB);
}