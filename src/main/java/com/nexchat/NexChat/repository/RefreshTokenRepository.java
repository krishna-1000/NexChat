package com.nexchat.NexChat.repository;

import com.nexchat.NexChat.modal.entity.RefreshToken;
import com.nexchat.NexChat.modal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserUsername(String username);
    @Modifying
    int deleteByUser(User user);
}
