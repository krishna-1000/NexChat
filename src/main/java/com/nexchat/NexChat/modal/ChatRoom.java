package com.nexchat.NexChat.modal;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean isprivate = true;
    private Long createdAt = new Date().getTime();

    @Column(nullable = false)
    private Long createdBy;

    @OneToMany(mappedBy = "chatRoom")
    private Set<ChatRoomMember> members = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages = new HashSet<>();


}
