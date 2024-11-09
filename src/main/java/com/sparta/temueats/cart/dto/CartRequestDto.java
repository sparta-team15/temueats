package com.sparta.temueats.cart.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CartRequestDto {

    @Column(nullable = false)
    @Min(value = 1, message = "수량 입력은 최소 1이상 가능합니다.")
    @Max(value = 50, message = "수량 입력은 최대 50까지 가능합니다.")
    private Long quantity;
}
