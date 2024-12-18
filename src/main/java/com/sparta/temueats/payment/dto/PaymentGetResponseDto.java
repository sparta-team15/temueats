package com.sparta.temueats.payment.dto;

import com.sparta.temueats.payment.entity.P_payment;
import com.sparta.temueats.payment.entity.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class PaymentGetResponseDto {
    private UUID paymentId;
    private PaymentStatus paymentStatus;
    private Long price;

    public PaymentGetResponseDto(P_payment payment) {
        this.paymentId = payment.getPaymentId();
        this.paymentStatus = payment.getPaymentStatus();
        this.price = payment.getPrice();
    }
}
