package com.sparta.temueats.store.service;

import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.store.dto.StoreUpdateDto;
import com.sparta.temueats.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;

    public void update(StoreUpdateDto storeUpdateDto) {
        storeRepository.findById(storeUpdateDto.getStoreId())
                .orElseThrow(() -> new CustomApiException("존재하지 않는 음식점"))
                .update(storeUpdateDto);
    }

}
