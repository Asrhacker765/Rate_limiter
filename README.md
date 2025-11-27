# Notification Engine - High-Throughput Cloud-Agnostic System

A production-ready Spring Boot application demonstrating:
- **Cloud-agnostic architecture** (AWS/GCP switching via Strategy Pattern)
- **High-throughput processing** (4.5B daily events via Kafka)
- **Distributed rate limiting** (Redis-based Token Bucket)
- **Async processing** (Decoupled ingestion from processing)

## Architecture

- **Controller**: Accepts requests and publishes to Kafka (non-blocking)
- **Consumer**: Processes messages asynchronously from Kafka
- **Storage Service**: Cloud-agnostic abstraction (AWS S3 / GCP Storage)
- **Rate Limiter**: Redis-based distributed rate limiting

## Prerequisites

- Java 17+
- Maven 3.6+
- Docker & Docker Compose

## Quick Start

### 1. Start Infrastructure

```bash
docker-compose up -d
```

This starts:
- Kafka (port 9092)
- Zookeeper (port 2181)
- Redis (port 6379)

### 2. Build and Run Application

```bash
mvn clean install
mvn spring-boot:run
```

### 3. Test the System

#### Send a Notification
```bash
curl -X POST "http://localhost:8080/api/notifications/send?userId=user1&message=Hello%20World"
```

#### Test Rate Limiting
```bash
# Send 15 requests rapidly
for i in {1..15}; do
  curl -X POST "http://localhost:8080/api/notifications/send?userId=user1&message=Test$i"
  echo ""
done
```

Expected behavior:
- Requests 1-10: "Processing email..."
- Requests 11+: "Rate limit exceeded..."

### 4. Switch Cloud Providers

Edit `src/main/resources/application.properties`:

```properties
# For AWS (default)
app.cloud.provider=aws

# For GCP
app.cloud.provider=gcp
```

Restart the application and check logs:
- AWS mode: `[AWS S3] Uploading email log...`
- GCP mode: `[GCP Storage] Uploading email log...`

## Project Structure

```
src/main/java/com/client/notification/
├── NotificationEngineApplication.java    # Main Spring Boot app
├── controller/
│   └── NotificationController.java      # REST endpoint
├── messaging/
│   └── NotificationConsumer.java        # Kafka consumer
└── service/
    ├── StorageService.java               # Cloud-agnostic interface
    ├── RateLimiterService.java           # Redis rate limiter
    └── impl/
        ├── AwsStorageService.java        # AWS implementation
        └── GcpStorageService.java        # GCP implementation
```

## Key Design Patterns

1. **Strategy Pattern**: `StorageService` interface with AWS/GCP implementations
2. **Conditional Beans**: `@ConditionalOnProperty` for cloud provider switching
3. **Async Processing**: Kafka decouples request handling from processing
4. **Distributed Rate Limiting**: Redis-based token bucket algorithm

## Configuration

### Rate Limiting
Default: 10 requests per 60 seconds per user
- Modify `MAX_REQUESTS` and `WINDOW_SECONDS` in `RateLimiterService.java`

### Kafka Topics
Default topic: `email_notifications`
- Modify in `NotificationController` and `NotificationConsumer`

## Production Considerations

- Add Dead Letter Queue (DLQ) for failed messages
- Implement retry logic with exponential backoff
- Add monitoring and metrics (Prometheus, Grafana)
- Use connection pooling for Redis
- Configure Kafka consumer groups for horizontal scaling
- Add authentication/authorization
- Implement proper error handling and logging

