package com.client.notification.service.impl;

import com.client.notification.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
// This bean is only created if app.cloud.provider = gcp
@ConditionalOnProperty(name = "app.cloud.provider", havingValue = "gcp")
public class GcpStorageService implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(GcpStorageService.class);

    @Override
    public void uploadLog(String id, String content) {
        // In a real app, use Google Cloud Storage SDK here
        log.info("[GCP Storage] Uploading email log for ID: {} to Bucket...", id);
        try {
            Thread.sleep(100); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[GCP Storage] Upload Complete.");
    }
}

