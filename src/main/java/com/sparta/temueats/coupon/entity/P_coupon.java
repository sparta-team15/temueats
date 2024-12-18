package com.sparta.temueats.coupon.entity;

import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.user.entity.P_user;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "P_COUPON")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class P_coupon extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    private String name;

    @NotNull
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private int discountAmount;

    @NotNull
    private Boolean status;

    @Past
    private LocalDateTime usedAt;

    @NotNull
    private LocalDate expiredAt;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)  // 소유자
    private P_user owner;

    @ManyToOne
    @JoinColumn(name = "issuer_id", nullable = false)  // 발행자
    private P_user issuer;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)  // 하나의 주문과만 연결
    private P_order order;
}
