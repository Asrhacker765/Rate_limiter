package com.client.notification.service.impl;

import com.client.notification.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
// This bean is only created if app.cloud.provider = aws
@ConditionalOnProperty(name = "app.cloud.provider", havingValue = "aws")
public class AwsStorageService implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(AwsStorageService.class);

    @Override
    public void uploadLog(String id, String content) {
        // In a real app, use AWS SDK (S3) here
        log.info("[AWS S3] Uploading email log for ID: {} to Bucket...", id);
        try {
            Thread.sleep(100); // Simulate network latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[AWS S3] Upload Complete.");
    }
}

