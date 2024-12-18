package com.sparta.temueats.order.entity;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.payment.entity.P_payment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_ORDER")
public class P_order extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID orderId;

    @Column(nullable = false)
    private UUID orderUId;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private boolean IsDelivery;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @Column
    private Long discountPrice;

    @Column
    @Size(max = 50, message = "요청 사항은 50글자 이내만 가능합니다.")
    private String customerRequest;

    @Column(nullable = false)
    private boolean cancelYn;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<P_cart> cartList;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private P_payment payment;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long ownerId;


    @Builder
    public P_order(UUID orderUId,Long amount, boolean IsDelivery, OrderState orderState, Long discountPrice, String customerRequest, boolean cancelYn, List<P_cart> cartList, P_payment payment, Long customerId, Long ownerId) {
        this.orderUId = orderUId;
        this.amount = amount;
        this.IsDelivery = IsDelivery;
        this.orderState = orderState;
        this.discountPrice = discountPrice;
        this.customerRequest = customerRequest;
        this.cancelYn = cancelYn;
        this.cartList = cartList;
        this.payment = payment;
        this.customerId = customerId;
        this.ownerId = ownerId;

    }

    public void updateDiscountPrice(int discount) {
        this.discountPrice = (long) discount;
    }

    public void updateAmount(Long finalTotal) {
        this.amount = finalTotal;
    }

    public void updateStatus(OrderState status) {
        this.orderState = status;
    }

    // 연관관계 편의 메소드
    public void setPayment(P_payment payment) {
        this.payment = payment;
    }

    //OrderState상태 변경
    public void changeOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    //cancleYn변경
    public void changeCancleYn() {
        this.cancelYn = false;
    }
}
