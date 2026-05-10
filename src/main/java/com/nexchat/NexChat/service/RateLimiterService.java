package com.nexchat.NexChat.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private static final long MAX_REQUESTS_PER_HOUR = 10;
    private final ConcurrentHashMap<Long, Bucket> userBuckets = new ConcurrentHashMap<>();

    private Bucket resolveBucket(Long userId) {
        return userBuckets.computeIfAbsent(userId, id -> {
            Refill refill = Refill.greedy(
                    MAX_REQUESTS_PER_HOUR,
                    Duration.ofHours(1)
            );
            Bandwidth limit = Bandwidth.classic(
                    MAX_REQUESTS_PER_HOUR,
                    refill
            );
            return Bucket.builder()
                    .addLimit(limit)
                    .build();
        });
    }


    public boolean tryConsume(Long userId) {
        Bucket bucket = resolveBucket(userId);
        return bucket.tryConsume(1);
    }


    public long getRemainingRequests(Long userId) {
        Bucket bucket = resolveBucket(userId);
        return bucket.getAvailableTokens();
    }


    public long getSecondsUntilRefill(Long userId) {
        Bucket bucket = resolveBucket(userId);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            return 0;
        }
        return probe.getNanosToWaitForRefill() / 1_000_000_000;
    }
}