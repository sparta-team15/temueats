package com.sparta.temueats.store.repository;

import com.sparta.temueats.store.dto.StoreResDto;
import com.sparta.temueats.store.entity.P_store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<P_store, UUID> {

   @Query("SELECT new com.sparta.temueats.store.dto.StoreResDto(s, " +
           "COALESCE(AVG(r.score), 0), " +
           "CAST(COUNT(rv) AS INTEGER), " +
           "(CASE WHEN EXISTS (SELECT 1 FROM P_FAV_STORE f WHERE f.user.id = :userId AND f.store = s) THEN true ELSE false END)) " +
           "FROM P_STORE s " +
           "LEFT JOIN P_review rv ON rv.store = s " +
           "LEFT JOIN P_RATING r ON r.store = s " +
           "WHERE s.name LIKE %:name% " +
           "GROUP BY s")
    List<StoreResDto> findByNameContaining(String name, Long userId);

    List<P_store> findByName(String name);

}
