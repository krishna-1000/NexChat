package com.nexchat.NexChat.repository;

import com.nexchat.NexChat.modal.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findTop50ByChatRoomIdOrderBySentAtDesc(Long chatRoomId);
}
