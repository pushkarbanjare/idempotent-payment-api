package com.pushkar.paymentapi.core;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component 
public class IdempotencyStore {
    private static final Object PROCESSING = new Object();
    private static final long TTL_MILLIS = 24 * 60 * 60 * 1000;

    private record Entry(Object value, long createdAt) {}

    private final ConcurrentHashMap<String, Entry> store = new ConcurrentHashMap<>();

    public boolean tryBeginProcessing(String idempotencyKey) {
        evictIfExpired(idempotencyKey);
        return store.putIfAbsent(idempotencyKey, new Entry(PROCESSING, System.currentTimeMillis())) == null;
    }

    public boolean isDuplicate(String idempotencyKey) {
        return store.containsKey(idempotencyKey);
    }
    
    public void save(String idempotencyKey, Object result) {
        store.put(idempotencyKey, new Entry(result, System.currentTimeMillis()));
    }
    
    public Object getStoredResult(String idempotencyKey) {
        evictIfExpired(idempotencyKey);
        Entry entry = store.get(idempotencyKey);
        if (entry == null || entry.value() == PROCESSING) return null;
        return entry.value();
    }

    private void evictIfExpired(String key) {
        Entry entry = store.get(key);
        if (entry != null && System.currentTimeMillis() - entry.createdAt() > TTL_MILLIS) {
            store.remove(key);
        }
    }
}
