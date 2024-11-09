package com.sparta.temueats.store.entity;

import com.sparta.temueats.global.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "P_MENU")
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

}
