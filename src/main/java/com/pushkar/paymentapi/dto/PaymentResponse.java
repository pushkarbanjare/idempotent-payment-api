package com.pushkar.paymentapi.dto;

import lombok.Getter;

@Getter 
public class PaymentResponse {
    private String transactionId;
    private String status;
    private boolean duplicate;

    public PaymentResponse(String transactionId, String status, boolean duplicate) {
        this.transactionId = transactionId;
        this.status = status;
        this.duplicate = duplicate;
    }
}
