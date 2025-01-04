package com.banbu.jh.service;

import com.banbu.jh.entity.RegionDong;
import com.banbu.jh.repository.RegionDongRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionCacheService {

    private final RegionDongRepository regionDongRepository;

    @Getter
    private List<RegionDong> allRegions;
    
    // city -> gu -> List<RegionDong> 형태로 캐시
    private Map<String, Map<String, List<RegionDong>>> regionsByCityAndGu;

    @PostConstruct
    public void init() {
        log.info("Initializing Region Cache...");
        loadRegions();
        log.info("Region Cache initialized with {} regions", allRegions.size());
    }

    private void loadRegions() {
        allRegions = regionDongRepository.findAll();
        
        // city와 gu별로 계층적 그룹화
        regionsByCityAndGu = allRegions.stream()
                .collect(Collectors.groupingBy(
                    RegionDong::getCity,
                    Collectors.groupingBy(RegionDong::getGu)
                ));
    }

    // 특정 city의 모든 구 목록 조회
    public List<String> getGuListByCity(String city) {
        return regionsByCityAndGu.getOrDefault(city, Map.of())
                .keySet()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    // 특정 city, gu의 모든 동 목록 조회
    public List<RegionDong> getDongListByCityAndGu(String city, String gu) {
        return regionsByCityAndGu
                .getOrDefault(city, Map.of())
                .getOrDefault(gu, List.of())
                .stream()
                .sorted(Comparator.comparing(region -> region.getId().getDong()))
                .collect(Collectors.toList());
    }

    // 모든 시 목록 조회
    public List<String> getAllCities() {
        return regionsByCityAndGu.keySet()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }
} 