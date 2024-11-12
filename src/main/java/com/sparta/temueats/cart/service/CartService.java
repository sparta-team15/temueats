package com.sparta.temueats.cart.service;

import com.sparta.temueats.cart.dto.CartUpdateRequestDto;
import com.sparta.temueats.cart.dto.CartUpdateResponseDto;
import com.sparta.temueats.cart.dto.CartGetListResponseDto;
import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.global.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    // todo 더미 데이터 싹 수정
    static final Long USER_ID1 = 1L;

    static final UUID MENU_ID = UUID.fromString("e95cb701-81b3-4124-9dd6-6b8fd423e787");

    static final UUID PRE_STORE_ID1 = UUID.randomUUID();
    static final UUID PRE_STORE_ID2 = UUID.randomUUID();

    private final CartRepository cartRepository;


    public CartUpdateResponseDto createCarts(CartUpdateRequestDto cartUpdateRequestDto, Long userId) {

        // 1. 장바구니가 비어 있으면 바로 담기 & 바로 리턴

        // 2-1. 장바구니에 같은 메뉴 id가 있는 경우
        boolean isPresentMenu = cartRepository.findByMenuIdByUserId(MENU_ID, USER_ID1).isPresent();
        if (isPresentMenu) {
            // 이미 같은 메뉴가 있다는 예외
            throw new CustomApiException("이미 해당 메뉴가 장바구니에 존재합니다.");
        }

        // 2-2. 담겨져 있는 메뉴의 가게 id와 새로 담는 메뉴의 가게 id가 다를 경우 예외
        // todo cart2.getMenu().getStore() 탐색 로직 추가해서 storeId 구해야 함.
        if (PRE_STORE_ID1.equals(PRE_STORE_ID2)) {
            throw new CustomApiException("서로 다른 가게 메뉴를 함께 담을 수 없습니다.");
        }

        // 3. 오류가 없으면 담기
        P_cart cart3 = cartRepository.save(P_cart.builder()
                        .quantity(cartUpdateRequestDto.getQuantity())
                        .selectYn(false)
                        .userId(USER_ID1)
                        .menuId(MENU_ID)
                        .deletedYn(false)
                .build());
        return new CartUpdateResponseDto(cart3);
    }

    public List<CartGetListResponseDto> getCarts() {
        List<P_cart> cartList = cartRepository.findAllByUserId(USER_ID1);
        List<CartGetListResponseDto> responseDtoList = new ArrayList<>();

        for (P_cart cart : cartList) {
            responseDtoList.add(new CartGetListResponseDto(cart));
        }

        return responseDtoList;
    }

    @Transactional
    public CartUpdateResponseDto updateCarts(CartUpdateRequestDto cartUpdateRequestDto, Long userId, UUID cartId) {
        Long updatePrice = cartUpdateRequestDto.getQuantity();

        if (updatePrice <= 0 || updatePrice > 50) {
            throw new CustomApiException("수량은 최소 1부터 50까지 변경할 수 있습니다.");
        }

        P_cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new CustomApiException("해당 장바구니 품목을 찾을 수 없습니다."));
        cart.update(cartUpdateRequestDto);

        return new CartUpdateResponseDto(cart);
    }

    @Transactional
    public void deleteCarts(Long userId, UUID cartId) {
        P_cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new CustomApiException("해당 장바구니 품목을 찾을 수 없습니다."));
        cart.delete();
    }

    @Transactional
    public void selectCarts(Long userId, UUID cartId) {
        P_cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new CustomApiException("해당 장바구니 품목을 찾을 수 없습니다."));
        cart.changeSelect();
    }
}