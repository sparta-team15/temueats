package com.sparta.temueats.store.service;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.store.dto.AddFavStoeRequestDto;
import com.sparta.temueats.store.dto.FavStoreListResponseDto;
import com.sparta.temueats.store.dto.StoreResDto;
import com.sparta.temueats.store.dto.StoreUpdateDto;
import com.sparta.temueats.store.entity.P_favStore;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.repository.FavStoreRepository;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final FavStoreRepository favStoreRepository;
    private final UserService userService;

    public void update(StoreUpdateDto storeUpdateDto, P_user user) {
        storeRepository.findById(storeUpdateDto.getStoreId())
                .orElseThrow(() -> new CustomApiException("존재하지 않는 음식점입니다."))
                .update(storeUpdateDto, user);

    }

    public List<StoreResDto> findByName(String name) {
        return storeRepository.findByNameContaining(name);
    }

    public Optional<P_store> findById(UUID storeId) {
        return storeRepository.findById(storeId);
    }

    // 가게 즐겨찾기 추가/삭제
    public ResponseDto addFavStore(AddFavStoeRequestDto requestDto, HttpServletRequest req) {

        // 사용자 검증
        P_user user = userService.validateTokenAndGetUser(req).orElse(null);
        if(user == null) {
            return new ResponseDto<>(-1, "유효하지 않은 토큰이거나 존재하지 않는 사용자입니다", null);
        }
        // 가게 검증
        P_store store = findById(requestDto.getStoreId()).orElse(null);
        if(store == null) {
            return new ResponseDto<>(-1, "존재하지 않는 가게입니다", null);
        }
        // 즐겨찾기 여부 확인
        Optional<P_favStore> existingFavStore = favStoreRepository.findByUserAndStore(user, store);

        if (existingFavStore.isPresent()) {
            // 이미 즐겨찾기에 있다면 삭제
            favStoreRepository.delete(existingFavStore.get());
            return new ResponseDto<>(1, "즐겨찾기에서 삭제되었습니다", null);
        } else {
            // 즐겨찾기에 없다면 추가
            P_favStore favStore = P_favStore.builder()
                    .user(user)
                    .store(store)
                    .build();
            favStoreRepository.save(favStore);
            return new ResponseDto<>(1, "즐겨찾기에 추가되었습니다", null);
        }

    }


    // 즐겨찾기 가게 목록 조회
    public ResponseDto getFavStoreList(HttpServletRequest req) {
        // 사용자 검증
        P_user user = userService.validateTokenAndGetUser(req).orElse(null);
        if(user == null) {
            return new ResponseDto<>(-1, "유효하지 않은 토큰이거나 존재하지 않는 사용자입니다", null);
        }
        // 즐겨찾기 가게 목록 조회
        List<P_store> favStores = favStoreRepository.findByUser(user);
        List<FavStoreListResponseDto> stores = favStores.stream()
                .map(FavStoreListResponseDto::new)
                .toList();

        return new ResponseDto(1, "즐겨찾기 가게 목록 조회 성공", stores);
    }
}
