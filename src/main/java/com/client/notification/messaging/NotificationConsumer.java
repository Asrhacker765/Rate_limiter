package com.client.notification.messaging;

import com.client.notification.service.RateLimiterService;
import com.client.notification.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    private final StorageService storageService; // Injected based on config (AWS or GCP)
    private final RateLimiterService rateLimiter;

    public NotificationConsumer(StorageService storageService, RateLimiterService rateLimiter) {
        this.storageService = storageService;
        this.rateLimiter = rateLimiter;
    }

    @KafkaListener(topics = "email_notifications", groupId = "email_worker_group")
    public void consume(String message) {
        // Message format assumed: "userId:emailContent"
        String[] parts = message.split(":", 2);
        String userId = parts[0];
        String content = parts.length > 1 ? parts[1] : "";

        // 1. Check Rate Limit (Distributed Check via Redis)
        if (!rateLimiter.isAllowed(userId)) {
            log.warn("Rate limit exceeded for User: {}. Re-queuing or dropping.", userId);
            // In real life: Send to Dead Letter Queue (DLQ)
            return;
        }

        // 2. Process "Sending" the email
        log.info("Processing email for User: {}", userId);

        // 3. Archive Log to Cloud Storage (Agnostic)
        storageService.uploadLog(userId, "Log for: " + content);
    }
}

