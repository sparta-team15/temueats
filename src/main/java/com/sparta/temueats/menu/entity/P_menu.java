package com.sparta.temueats.menu.entity;

import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.SellState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity(name = "P_MENU")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class P_menu extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private P_store store;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer price;

    private String image;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private SellState sellState;

    @Column(nullable = false)
    private Boolean signatureYn;

    @Builder
    public P_menu(P_store store, String name, String description, Integer price, String image, Category category, SellState sellState, Boolean signatureYn) {
        this.store = store;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.sellState = sellState;
        this.signatureYn = signatureYn;
    }
}
