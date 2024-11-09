package com.sparta.temueats.store.repository;

import com.sparta.temueats.store.entity.P_store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<P_store, UUID> {

}
