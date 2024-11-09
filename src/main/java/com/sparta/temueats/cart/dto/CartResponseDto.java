package com.sparta.temueats.cart.dto;

import com.sparta.temueats.cart.entity.P_cart;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CartResponseDto {
    private UUID cartId;
    private Long quantity;
    private boolean paidYn;

    public CartResponseDto(P_cart pCart) {
        this.cartId = pCart.getCartId();
        this.quantity = pCart.getQuantity();
        this.paidYn = false;
    }
}
