package com.banbu.jh.service.impl;

import com.banbu.jh.feign.AptTradeFeignClient;
import com.banbu.jh.dto.AptLottoResponse;
import com.banbu.jh.entity.AptLottoPlan;
import com.banbu.jh.repository.AptLottoPlanRepository;
import com.banbu.jh.service.AptLottoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AptLottoServiceImpl implements AptLottoService {
    private final AptTradeFeignClient aptTradeFeignClient;
    private final AptLottoPlanRepository aptLottoPlanRepository;
    
    @Value("${apt.trade.service.key}")
    private String serviceKey;
    
    @Override
    @Transactional
    public void collectAndSaveAptLottoData() {
        log.info("Starting apt lotto data collection");
        
        // 기존 데이터 전체 삭제
        aptLottoPlanRepository.deleteAll();
        log.info("Deleted all existing apt lotto data");
        
        int page = 1;
        int perPage = 5000;
        List<AptLottoPlan> allPlans = new ArrayList<>();
        
        try {
            AptLottoResponse response = aptTradeFeignClient.getAptLottoData(page, perPage, serviceKey);
            
            for (AptLottoResponse.Item item : response.getData()) {
                AptLottoPlan plan = convertToAptLottoPlan(item);
                allPlans.add(plan);
            }
            
            // 2000개씩 나누어 저장
            int batchSize = 2000;
            for (int i = 0; i < allPlans.size(); i += batchSize) {
                int endIndex = Math.min(i + batchSize, allPlans.size());
                List<AptLottoPlan> batch = allPlans.subList(i, endIndex);
                aptLottoPlanRepository.saveAll(batch);
                log.info("Saved batch {} - {} of {}", i, endIndex, allPlans.size());
            }
            
            log.info("Completed apt lotto data collection. Total saved: {}", allPlans.size());
            
        } catch (Exception e) {
            log.error("Error while collecting apt lotto data: ", e);
            throw new RuntimeException("Failed to collect apt lotto data", e);
        }
    }
    
    private AptLottoPlan convertToAptLottoPlan(AptLottoResponse.Item item) {
        return AptLottoPlan.builder()
                .id(Long.parseLong(item.getHOUSE_MANAGE_NO()))
                .region(item.getSUBSCRPT_AREA_CODE_NM())
                .reciptBegin(LocalDate.parse(item.getRCEPT_BGNDE()))
                .reciptEnd(LocalDate.parse(item.getRCEPT_ENDDE()))
                .houseNm(item.getHOUSE_NM())
                .type(item.getHOUSE_DTL_SECD_NM())
                .url(item.getPBLANC_URL())
                .businessName(item.getBSNS_MBY_NM())
                .builderName(item.getCNSTRCT_ENTRPS_NM())
                .totalSupply(Integer.parseInt(item.getTOT_SUPLY_HSHLDCO()))
                .address(item.getHSSPLY_ADRES())
                .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AptLottoPlan> findLatestByRegion(String region) {
        log.info("Fetching latest apt lotto plans for region: {}", region);
        if ("전체".equals(region)) {
            return aptLottoPlanRepository.findAll();
        } else {
            return aptLottoPlanRepository.findAllByRegion(region);
        }
    }
} 