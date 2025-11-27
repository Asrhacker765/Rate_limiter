#!/bin/bash

# Test script for Notification Engine
# Usage: ./test.sh [userId] [numRequests]

USER_ID=${1:-user1}
NUM_REQUESTS=${2:-15}

echo "Testing Notification Engine..."
echo "User ID: $USER_ID"
echo "Number of requests: $NUM_REQUESTS"
echo ""

for i in $(seq 1 $NUM_REQUESTS); do
  echo "Request $i:"
  curl -s -X POST "http://localhost:8080/api/notifications/send?userId=$USER_ID&message=Test$i"
  echo ""
  sleep 0.1
done

echo ""
echo "Test completed! Check application logs for rate limiting behavior."

