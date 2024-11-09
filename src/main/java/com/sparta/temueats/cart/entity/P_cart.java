package com.sparta.temueats.cart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_CART")
public class P_cart {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID cartId;

    @Column(nullable = false)
    @Min(value = 1, message = "수량 입력은 최소 1이상 가능합니다.")
    @Max(value = 50, message = "수량 입력은 최대 50까지 가능합니다.")
    private Long quantity;

    @Column(nullable = false)
    private boolean paidYn;

}
