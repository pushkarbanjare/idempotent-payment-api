package com.pushkar.paymentapi.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.pushkar.paymentapi.dto.PaymentRequest;
import com.pushkar.paymentapi.dto.PaymentResponse;
import com.pushkar.paymentapi.service.PaymentService;

import jakarta.validation.Valid;

@RestController 
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) { 
        this.paymentService = paymentService; 
    }

    @PostMapping("/api/payments")
    public ResponseEntity<PaymentResponse> createPayment(
        @RequestHeader("Idempotency-Key") String idempotencyKey, 
        @Valid @RequestBody PaymentRequest request) {
            PaymentResponse response = paymentService.process(idempotencyKey, request);
            return ResponseEntity.ok(response);
        }
}
