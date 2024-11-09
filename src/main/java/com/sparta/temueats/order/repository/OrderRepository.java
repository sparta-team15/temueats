package com.sparta.temueats.order.repository;

import com.sparta.temueats.order.entity.P_order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<P_order, Long> {
}
