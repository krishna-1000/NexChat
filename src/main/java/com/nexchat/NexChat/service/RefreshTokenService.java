package com.nexchat.NexChat.service;

import com.nexchat.NexChat.modal.entity.RefreshToken;
import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.RefreshTokenRepository;
import com.nexchat.NexChat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    @Value("${nexchat.jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        return refreshToken;
    }

    @Transactional
    public synchronized RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByUserUsername(username)
                .orElse(new RefreshToken());
        if (refreshToken.getUser() == null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));
            refreshToken.setUser(user);
        }
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(refreshTokenRepository::deleteByUser)
                .orElse(0);
    }
}