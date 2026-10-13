package com.pushkar.paymentapi.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class IdempotencyStoreConcurrencyTest {
    
    @Test 
    @Timeout(10) 
    void onlyOneThreadShouldProcessForTheSameKey() throws InterruptedException {
        IdempotencyStore store = new IdempotencyStore();
        String sameKey = "duplicate-key";
        int threadCount = 20;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(threadCount);

        AtomicInteger timesProcessed = new AtomicInteger(0);

        for(int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startSignal.await();
                    if(store.tryBeginProcessing(sameKey)) {
                        timesProcessed.incrementAndGet();
                        store.save(sameKey, "payment-success");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneSignal.countDown();
                }
            });
        }
        startSignal.countDown();
        doneSignal.await();
        executor.shutdown();
        assertEquals(1, timesProcessed.get(), "exactly one thread should process a payment for the same key, others must be rejected");
    }
}
