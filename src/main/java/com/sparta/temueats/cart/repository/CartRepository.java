package com.sparta.temueats.cart.repository;

import com.sparta.temueats.cart.entity.P_cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<P_cart, Long> {
}
