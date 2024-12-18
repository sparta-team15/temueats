package com.sparta.temueats.menu.entity;

import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.menu.dto.MenuUpdateDto;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.SellState;
import com.sparta.temueats.user.entity.P_user;
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

    public P_menu update(MenuUpdateDto menuUpdateDto, P_user user) {
        if (menuUpdateDto.getName() != null) {
            name = menuUpdateDto.getName();
        }
        if (menuUpdateDto.getDescription() != null) {
            description = menuUpdateDto.getDescription();
        }
        if (menuUpdateDto.getPrice() != null) {
            price = menuUpdateDto.getPrice();
        }
        if (menuUpdateDto.getImage() != null) {
            image = menuUpdateDto.getImage();
        }
        if (menuUpdateDto.getCategory() != null) {
            category = menuUpdateDto.getCategory();
        }
        if (menuUpdateDto.getSellState() != null) {
            sellState = menuUpdateDto.getSellState();
        }
        if (menuUpdateDto.getSignatureYn() != null) {
            signatureYn = menuUpdateDto.getSignatureYn();
        }
        
        setUpdatedBy(user.getNickname());

        return this;
    }

}
