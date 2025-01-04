package com.banbu.jh.service.impl;

import com.banbu.jh.repository.RegionCodeRepository;
import com.banbu.jh.repository.RegionDongRepository;
import org.springframework.stereotype.Service;

import com.banbu.jh.entity.RegionCode;
import com.banbu.jh.entity.RegionDong;
import com.banbu.jh.service.RegionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionCodeRepository regionCodeRepository;
    private final RegionDongRepository regionDongRepository;
    
    public RegionCode getRegionByGuCode(Integer guCode) {
        return regionCodeRepository.findByCityAndGu(null, null)
                .orElseThrow(() -> new IllegalArgumentException("Invalid region code: " + guCode));
    }
    
    public List<RegionDong> getDongsByGuCode(Integer guCode) {
        return regionDongRepository.findByIdGuCode(guCode);
    }

}
