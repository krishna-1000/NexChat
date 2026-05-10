package com.nexchat.NexChat.repository;

import com.nexchat.NexChat.modal.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    List<ChatRoom> findByPrivateRoom(boolean type);

}
