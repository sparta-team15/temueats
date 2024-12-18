package com.sparta.temueats.order.service;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.coupon.entity.P_coupon;
import com.sparta.temueats.coupon.repository.CouponRepository;
import com.sparta.temueats.coupon.service.CouponService;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.order.dto.DeliveryOrderCreateRequestDto;
import com.sparta.temueats.order.dto.OrderGetResponseDto;
import com.sparta.temueats.order.entity.OrderState;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.order.repository.OrderRepository;
import com.sparta.temueats.payment.entity.PaymentStatus;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCustomerService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final UserRepository userRepository;

    @Transactional
    public void createDeliveryOrders(DeliveryOrderCreateRequestDto deliveryOrderCreateRequestDto, P_user user) {
        // 0. 상태가 STANDBY 인 주문이 존재하면, 주문 생성 불가
        List<P_order> IsIngOrder = orderRepository.findAllByUserIdIsIng(user.getId(), OrderState.STANDBY);
        if (!IsIngOrder.isEmpty()) {
            throw new CustomApiException("현재 진행중인 주문이 있습니다. 해당 주문에 대한 결제를 먼저 진행해주세요.");
        }

        // 1. 주문 생성 시 장바구니에서 선택된 물품들 가져오기
        List<P_cart> allBySelect = cartRepository.findAllBySelectAndUserId(user.getId());

        if (allBySelect.isEmpty()) {
            throw new CustomApiException("장바구니에서 주문할 메뉴를 하나 이상 선택해주세요.");
        }

        // 2. 가져와서 가격 취합 후 db에 저장
        Long total = 0L;
        Long ownerId = 0L;
        Long leastPrice = 0L;
        Long deliveryPrice = 0L;
        for (P_cart cart : allBySelect) {
            total += cart.getMenu().getPrice().longValue() * cart.getQuantity();
            ownerId = cart.getMenu().getStore().getUser().getId();
            leastPrice = Long.valueOf(cart.getMenu().getStore().getLeastPrice());
            deliveryPrice = Long.valueOf(cart.getMenu().getStore().getDeliveryPrice());
        }

        // +) 주문 금액이 가게의 leastPrice 미만이면 예외
        if (total < leastPrice) {
            throw new CustomApiException("가게의 최소 주문 금액을 확인해주세요.");
        }

        // +) 주문 생성 직전 가격에 deliveryPrice 추가
        total += deliveryPrice;

        // 3. 주문 객체 생성 및 저장
        P_order order = P_order.builder()
                .orderUId(UUID.randomUUID())
                .amount(total)
                .IsDelivery(true)
                .orderState(OrderState.STANDBY)
                .discountPrice(0L)
                .customerRequest(deliveryOrderCreateRequestDto.getCustomerRequest())
                .cancelYn(true)
                .cartList(allBySelect)
                .customerId(user.getId())
                .ownerId(ownerId)
                .build();

        order = orderRepository.save(order);


        // 4. 쿠폰 입력이 존재하면, 입력된 쿠폰이 해당 유저의 쿠폰인지 검증하고 사용
        if (deliveryOrderCreateRequestDto.getCouponId() != null) {
            P_coupon coupon = couponRepository.findById(deliveryOrderCreateRequestDto.getCouponId()).orElseThrow(() ->
                    new CustomApiException("해당 쿠폰 아이디를 조회할 수 없습니다."));

            if (coupon.getOwner().getId().equals(user.getId())) {
                int discount = coupon.getDiscountAmount();
                Long finalTotal = total - discount;

                couponService.useCoupon(coupon.getId(), order);

                // 4-1 쿠폰 할인 금액 및 최종 금액을 적용하여 다시 저장
                order.updateDiscountPrice(discount);
                order.updateAmount(finalTotal);

            }
        }
    }

    public List<OrderGetResponseDto> getCustomerOrders(P_user user) {
        List<P_order> orderList = orderRepository.findAllByCustomerId(user.getId());
        List<OrderGetResponseDto> responseDtoList = new ArrayList<>();

        for (P_order order : orderList) {
            responseDtoList.add(new OrderGetResponseDto(order));
        }

        return responseDtoList;
    }

    @Transactional
    public void cancelCustomerOrder(UUID orderId) {
        // 1. 주문 테이블에서 해당 orderId 를 찾아옴
        P_order order = orderRepository.findById(orderId).orElseThrow(() ->
                new CustomApiException("해당 주문 내역을 찾을 수 없습니다."));

        // 2. 해당 orderId의 cancelYn 체크
        if (!order.isCancelYn()) {
            throw new CustomApiException("결제 후 5분이 지나 주문을 취소할 수 없습니다.");
        }

        // 2-1. 주문 상태 변경 후 취소 정보 추가
        order.updateStatus(OrderState.FAIL);

        log.info("주문 상태 변경까지 완료");

        // 2-2. 쿠폰 적용 취소하기
        Optional<P_coupon> usedCoupon = couponRepository.findCouponByOrderId(order.getOrderId());
        if (usedCoupon.isPresent()) {
            couponService.cancelCoupon(usedCoupon.get().getId());
        }

        // 3. 결제 상태를 canceled 로 설정하고 취소일시, 취소자에 정보 추가
        order.getPayment().setStatus(PaymentStatus.CANCELED);

        P_user user = userRepository.findById(order.getCustomerId()).orElseThrow(() ->
                new CustomApiException("해당 유저를 찾을 수 없습니다."));
        order.getPayment().setUpdatedAt(LocalDateTime.now());
        order.getPayment().setUpdatedBy(user.getEmail());

    }
}
