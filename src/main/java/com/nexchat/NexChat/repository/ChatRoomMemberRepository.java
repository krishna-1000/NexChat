package com.nexchat.NexChat.repository;

import com.nexchat.NexChat.modal.entity.ChatRoom;
import com.nexchat.NexChat.modal.entity.ChatRoomMember;
import com.nexchat.NexChat.modal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    @Query("SELECT m.chatRoom.id FROM ChatRoomMember m " +
            "JOIN m.chatRoom c " +
            "WHERE m.user.id IN (:userA, :userB) " +
            "AND c.privateRoom = true " +
            "GROUP BY m.chatRoom.id " +
            "HAVING COUNT(DISTINCT m.user.id) = 2")
    Optional<Long> findPrivateChatRoomId(@Param("userA") Long userA, @Param("userB") Long userB);

    @Query("SELECT c FROM ChatRoomMember m " +
            "JOIN m.chatRoom c " +
            "WHERE m.user.id = :userA " +
            "AND c.privateRoom = false")
    List<ChatRoom> findAllGroupsForUser(@Param("userA") Long userA);

    @Transactional
    @Modifying
    @Query("delete from ChatRoomMember m where m.user.id = :memberId and m.chatRoom.id = :groupId")
    void exitMember(@Param("memberId") Long memberId,@Param("groupId") Long groupId);

    boolean existsByUserAndChatRoom(User sender,ChatRoom chatRoom);
}