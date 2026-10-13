package com.pushkar.paymentapi.core;

import java.util.concurrent.ConcurrentHashMap;

public class IdempotencyStore {
    private final ConcurrentHashMap<String, Object> store = new ConcurrentHashMap<>();
    public boolean isDuplicate(String idempotencyKey) {
        return store.containsKey(idempotencyKey);
    }
    public void save(String idempotencyKey, Object result) {
        store.put(idempotencyKey, result);
    }
    public Object getStoredResult(String idempotencyKey) {
        return store.get(idempotencyKey);
    }
}
