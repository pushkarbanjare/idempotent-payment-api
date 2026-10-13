package com.pushkar.paymentapi.core;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component 
public class IdempotencyStore {
    private static final Object PROCESSING = new Object();
    private final ConcurrentHashMap<String, Object> store = new ConcurrentHashMap<>();
    public boolean tryBeginProcessing(String idempotencyKey) {
        return store.putIfAbsent(idempotencyKey, PROCESSING) == null;
    }
    public boolean isDuplicate(String idempotencyKey) {
        return store.containsKey(idempotencyKey);
    }
    public void save(String idempotencyKey, Object result) {
        store.put(idempotencyKey, result);
    }
    public Object getStoredResult(String idempotencyKey) {
        Object value = store.get(idempotencyKey);
        return value == PROCESSING ? null : value;
    }
}
