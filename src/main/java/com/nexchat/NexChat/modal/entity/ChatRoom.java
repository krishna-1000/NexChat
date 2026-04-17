package com.nexchat.NexChat.modal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String name;
    private boolean isprivate = true;
    private Long createdAt = new Date().getTime();

    @Column(nullable = false)
    private String createdBy;

    @OneToMany(mappedBy = "chatRoom",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ChatRoomMember> members = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom" , cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sentAt ASC")
    private Set<Message> messages = new HashSet<>();


}
