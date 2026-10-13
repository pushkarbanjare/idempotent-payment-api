# Idempotent Payment API

A Spring Boot payment API that safely handles duplicate/retried requests using the Idempotency Key pattern — the same approach Stripe uses in production.

## Why

Network retries and double-clicks can cause the same payment request to reach the server twice. Without protection, that means charging a customer twice. This API lets clients send an `Idempotency-Key` header; a retried request with the same key returns the original result instead of processing again.

## How it works

- Client sends a unique `Idempotency-Key` with each payment request.
- First time a key is seen: payment is "processed" (simulated), result stored.
- Same key seen again: stored result returned immediately, no reprocessing.
- Keys expire after 24 hours (TTL), matching real-world retry windows.

## Tech

- Java 21, Spring Boot 3.5, Lombok
- Thread-safe via `ConcurrentHashMap.putIfAbsent` (atomic check-and-claim)
- JUnit 5, including a concurrency test proving no double-processing under load

## Run locally

```bash
./mvnw spring-boot:run
```

## Try it

```bash
curl -X POST "http://localhost:8080/api/payments" -H "Content-Type: application/json" -H "Idempotency-Key: key-001" -d "{\"amount\": 500, \"payee\": \"merchant-A\"}"; echo
```

Repeat the exact same command — you'll get the same `transactionId` back with `"duplicate":true`.