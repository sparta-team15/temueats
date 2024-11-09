package com.sparta.temueats.cart.service;

import com.sparta.temueats.cart.dto.CartRequestDto;
import com.sparta.temueats.cart.dto.CartResponseDto;
import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.global.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    // todo 더미 데이터 싹 수정
    static final Long USER_ID = 1L;
    static final UUID PRE_STORE_ID1 = UUID.randomUUID();
    static final UUID PRE_STORE_ID2 = UUID.randomUUID();

    private final CartRepository cartRepository;

    public CartResponseDto createCarts(CartRequestDto cartRequestDto, Long userId) {
        // 1. 유저의 장바구니가 비어있을 경우
        boolean isPresentCart = cartRepository.findByUserId(USER_ID).isPresent();
        if (!isPresentCart) {
            // 바로 담기
            P_cart cart1 = cartRepository.save(new P_cart(cartRequestDto, USER_ID));
            return new CartResponseDto(cart1);
        }

        // 2. 유저의 장바구니가 비어있지 않을 경우

        // 2-1. 장바구니에 같은 메뉴 id가 있는 경우
        // boolean isPresentMenu = cartRepository.findByMenuId(MENU_ID).isPresent();
        boolean isPresentMenu = false;
        // todo 임시 데이터
        // boolean isPresentMenu = true;
        if (isPresentMenu) {
            // 이미 같은 메뉴가 있다는 예외
            throw new CustomApiException("이미 해당 메뉴가 장바구니에 존재합니다.");
        }

        // 2-2. 담겨져 있는 메뉴의 가게 id와 새로 담는 메뉴의 가게 id가 다를 경우 예외
//        P_cart cart2 = cartRepository.findByMenuId(MENU_ID).orElseThrow(() ->
//                new CustomApiException("해당 MENU_ID가 존재하지 않습니다."));
        // todo 임시 데이터
        // todo cart2.getMenu().getStore() 탐색 로직 추가해서 storeId 구해야 함.
        if (PRE_STORE_ID1.equals(PRE_STORE_ID2)) {
            throw new CustomApiException("서로 다른 가게 메뉴를 함께 담을 수 없습니다.");
        }

        // 3. 오류가 없으면 담기
        P_cart cart3 = cartRepository.save(new P_cart(cartRequestDto, USER_ID));
        return new CartResponseDto(cart3);
    }
}