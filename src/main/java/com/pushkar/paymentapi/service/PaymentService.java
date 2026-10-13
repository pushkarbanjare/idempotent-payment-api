package com.pushkar.paymentapi.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pushkar.paymentapi.core.IdempotencyStore;
import com.pushkar.paymentapi.dto.PaymentRequest;
import com.pushkar.paymentapi.dto.PaymentResponse;

@Service 
public class PaymentService {
    private final IdempotencyStore idempotencyStore;

    public PaymentService(IdempotencyStore idempotencyStore) { 
        this.idempotencyStore = idempotencyStore; 
    }

    public PaymentResponse process(String idempotencyKey, PaymentRequest request) {
        if(!idempotencyStore.tryBeginProcessing(idempotencyKey)) {
            Object storedResult = idempotencyStore.getStoredResult(idempotencyKey);
            if(storedResult instanceof PaymentResponse existing) {
                return new PaymentResponse(existing.getTransactionId(), existing.getStatus(), true);
            }
            return new PaymentResponse(null, "PROCESSING", true);
        }

        String transactionId = UUID.randomUUID().toString();
        PaymentResponse response = new PaymentResponse(transactionId, "SUCCESS", false);
        idempotencyStore.save(idempotencyKey, response);
        return response;
    }
}
