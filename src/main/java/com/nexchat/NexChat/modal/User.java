package com.nexchat.NexChat.modal;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private  String username;

    @Column(unique = true, nullable = false)
    private  String email;

    @Column(nullable = false)
    private  String password;

    private  String profilePictureUrl;


    private boolean isOnline = false;


    private Date lastSeen;
    private final Date createdAt = new Date();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<ChatRoomMember> chatRoomMemberships = new HashSet<>();



  
}
