package com.sparta.temueats.store.repository;

import com.sparta.temueats.store.entity.P_menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<P_menu, UUID> {
    
}
