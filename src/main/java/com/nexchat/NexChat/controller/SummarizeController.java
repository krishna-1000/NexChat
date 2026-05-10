package com.nexchat.NexChat.controller;


import com.nexchat.NexChat.service.RateLimiterService;
import com.nexchat.NexChat.service.SecurityService;
import com.nexchat.NexChat.service.SummarizeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class SummarizeController {

    private final RateLimiterService rateLimiterService;
    private final SummarizeService summarizeService;
    private final SecurityService securityService;

    public SummarizeController(RateLimiterService rateLimiterService, SummarizeService summarizeService, SecurityService securityService) {
        this.rateLimiterService = rateLimiterService;
        this.summarizeService = summarizeService;
        this.securityService = securityService;
    }

    @PostMapping("/{groupId}/summarize")
    public ResponseEntity<Map<String, Object>> summarize(
            @PathVariable Long groupId) {

        Long userId = securityService.getCurrentUserId();

        boolean allowed = rateLimiterService.tryConsume(userId);

        if (!allowed) {
            long remaining = rateLimiterService.getRemainingRequests(userId);
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of(
                            "error", "Rate limit exceeded",
                            "remainingChances", remaining,
                            "message", "You can summarize 5 times per hour. Please wait before trying again."
                    ));
        }

        String summary = summarizeService.summarizeGroup(groupId);
        long remainingChances = rateLimiterService.getRemainingRequests(userId);

        return ResponseEntity.ok(Map.of(
                "summary", summary,
                "remainingChances", remainingChances,
                "messageCount", 50
        ));
    }
}