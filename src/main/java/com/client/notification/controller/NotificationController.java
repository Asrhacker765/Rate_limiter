package com.client.notification.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public NotificationController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/send")
    public String sendNotification(@RequestParam String userId, @RequestParam String message) {
        // High throughput: We just dump it to Kafka and return immediately.
        // We do NOT process the email here.
        kafkaTemplate.send("email_notifications", userId + ":" + message);
        return "Notification Accepted for processing";
    }
}

