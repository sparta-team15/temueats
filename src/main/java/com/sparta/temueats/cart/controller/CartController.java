package com.sparta.temueats.cart.controller;

import com.sparta.temueats.cart.dto.CartResponseDto;
import com.sparta.temueats.cart.dto.CartRequestDto;
import com.sparta.temueats.cart.service.CartService;
import com.sparta.temueats.global.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    static final Long USER_ID = 1L;

    private final CartService cartService;

    // 장바구니 추가
    @PostMapping("/carts")
    public ResponseDto<?> cartResponseDto(@RequestBody @Valid CartRequestDto cartRequestDto, BindingResult bindingResult) {
        CartResponseDto cartResponseDto = cartService.createCarts(cartRequestDto, USER_ID);
        return new ResponseDto<>(1, "장바구니 추가가 완료되었습니다.", cartResponseDto);
    }

//    // 장바구니 전체 조회
//    @GetMapping("/carts")
//    public List<CartResponseDto> cartResponseDto() {
//        return cartService.getCarts(USERID);
//
//    }
//
//    // 장바구니 메뉴 개수 수정
//    @PutMapping("/carts/cart_id}/modify")
//
//    // 장바구니 삭제
//    @PutMapping("/carts/{cart_id}")
}
