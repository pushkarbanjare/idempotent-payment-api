package com.pushkar.paymentapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class PaymentRequest {
    @Positive(message = "amount must be positive")
    private double amount;

    @NotBlank(message = "payee must not be blank")
    private String payee;
}
