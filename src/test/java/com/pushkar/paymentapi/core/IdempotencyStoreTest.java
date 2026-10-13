package com.pushkar.paymentapi.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class IdempotencyStoreTest {
    
    @Test 
    void newKeyIsNotADuplicate() {
        IdempotencyStore store = new IdempotencyStore();
        assertFalse(store.isDuplicate("key-1"));
    }

    @Test 
    void savedKeyIsRecognizedAsDuplicate() {
        IdempotencyStore store = new IdempotencyStore();
        store.save("key-1", "payment-success");
        assertTrue(store.isDuplicate("key-1"));
    }

    @Test 
    void retrievesTheOriginalStoredResult() {
        IdempotencyStore store = new IdempotencyStore();
        store.save("key-1", "payment-success");
        assertEquals("payment-success", store.getStoredResult("key-1"));
    }

    @Test 
    void differentKeysAreIndependent() {
        IdempotencyStore store = new IdempotencyStore();
        store.save("key-1", "result-A");
        assertFalse(store.isDuplicate("key-2"));
        assertNull(store.getStoredResult("key-2"));
    }
}
