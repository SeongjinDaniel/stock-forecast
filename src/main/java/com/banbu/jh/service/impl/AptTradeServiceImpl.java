package com.banbu.jh.service.impl;

import com.banbu.jh.dto.AptTradeResponse;
import com.banbu.jh.entity.AptSales;
import com.banbu.jh.feign.AptTradeFeignClient;
import com.banbu.jh.service.AptTradeService;
import lombok.RequiredArgsConstructor;
import com.banbu.jh.repository.AptSalesRepository;
import com.banbu.jh.repository.RegionCodeRepository;
import com.banbu.jh.entity.RegionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.banbu.jh.repository.AptDealSalesRepository;
import com.banbu.jh.entity.AptDealSales;
import com.banbu.jh.dto.AptSalesProjection;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import java.util.Iterator;

@Slf4j
@Service
@RequiredArgsConstructor
public class AptTradeServiceImpl implements AptTradeService {

    private final AptTradeFeignClient aptTradeFeignClient;
    private final AptSalesRepository aptSalesRepository;
    private final RegionCodeRepository regionCodeRepository;
    private final AptDealSalesRepository aptDealSalesRepository;

    @Value("${apt.trade.service.key}")
    private String serviceKey;

    @Override
    public AptTradeResponse getAptTradeDetail(int pageNo, int numOfRows, String lawdCd, String dealYmd) {
        return aptTradeFeignClient.getAptTradeDetail(serviceKey, pageNo, numOfRows, lawdCd, dealYmd);
    }

    @Override
    @Transactional
    public void saveAptTradeData(String lawdCd, String dealYmd) {
        log.info("Starting to fetch and save apartment trade data for lawdCd: {}, dealYmd: {}", lawdCd, dealYmd);
        int pageNo = 1;
        int numOfRows = 1000;
        int totalSaved = 0;
        
        while (true) {
            log.info("Fetching page {} with {} rows per page", pageNo, numOfRows);
            AptTradeResponse response = aptTradeFeignClient.getAptTradeDetail(
                    serviceKey, pageNo, numOfRows, lawdCd, dealYmd);
            
            if (response.getBody().getItems() == null || 
                response.getBody().getItems().getItemList() == null || 
                response.getBody().getItems().getItemList().isEmpty()) {
                log.info("No more items to process. Ending data fetch.");
                break;
            }

            int currentPageSize = response.getBody().getItems().getItemList().size();
            log.info("Processing {} items from current page", currentPageSize);

            for (AptTradeResponse.Item item : response.getBody().getItems().getItemList()) {
                aptSalesRepository.save(AptSales.from(item, lawdCd));
                totalSaved++;
            }

            log.info("Successfully saved {} items from page {}", currentPageSize, pageNo);

            int totalCount = Integer.parseInt(response.getBody().getTotalCount());
            log.info("Total count of items: {}, Current progress: {}/{}", 
                    totalCount, totalSaved, totalCount);

            if (pageNo * numOfRows >= totalCount) {
                log.info("Reached the end of data. Total saved items: {}", totalSaved);
                break;
            }
            pageNo++;
        }
        log.info("Completed saving apartment trade data. Total items saved: {}", totalSaved);
    }

    @Override
    @Transactional
    public void saveAllRegionAptTradeData(String dealYmd) {
        log.info("Starting to fetch and save apartment trade data for all regions, dealYmd: {}", dealYmd);
        
        List<RegionCode> regionCodes = regionCodeRepository.findAll();
        int totalRegions = regionCodes.size();
        log.info("Total {} regions found", totalRegions);
        
        // 전체 데이터를 담을 리스트
        List<AptSales> totalAptSalesList = new ArrayList<>();
        int totalFetched = 0;
        
        // 1. 모든 지역의 데이터를 먼저 수집
        for (RegionCode regionCode : regionCodes) {
            if (regionCode != null && regionCode.getId() != null) {
                String lawdCd = String.format("%05d", regionCode.getId().getGuCode());
                // log.info("Fetching data for region: {} ({})", regionCode.getGu(), lawdCd);
                
                int pageNo = 1;
                int numOfRows = 1000;
                
                while (true) {
                    try {
                        AptTradeResponse response = aptTradeFeignClient.getAptTradeDetail(
                                serviceKey, pageNo, numOfRows, lawdCd, dealYmd);
                        
                        if (response.getBody().getItems() == null || 
                            response.getBody().getItems().getItemList() == null || 
                            response.getBody().getItems().getItemList().isEmpty()) {
                            break;
                        }

                        List<AptSales> currentPageItems = response.getBody().getItems().getItemList().stream()
                            .map(item -> AptSales.from(item, lawdCd))
                            .collect(Collectors.toList());
                            
                        totalAptSalesList.addAll(currentPageItems);
                        totalFetched += currentPageItems.size();
                            
                        // log.info("Fetched {} records from region {} (Total fetched: {})", 
                        //     currentPageItems.size(), regionCode.getGu(), totalFetched);

                        int totalCount = Integer.parseInt(response.getBody().getTotalCount());
                        if (pageNo * numOfRows >= totalCount) {
                            break;
                        }
                        pageNo++;
                        
                    } catch (Exception e) {
                        log.error("Error fetching data for region {}: {}", lawdCd, e.getMessage());
                        break;
                    }
                }
            } else {
                log.warn("Skipping null RegionCode or RegionCodeId");
                continue;
            }
        }
        
        log.info("Data fetching completed. Total records fetched: {}", totalFetched);
        
        // 2. 수집된 전체 데이터를 2000개씩 배치 저장
        try {
            int batchSize = 2000;
            int totalSaved = 0;
            
            for (int i = 0; i < totalAptSalesList.size(); i += batchSize) {
                int end = Math.min(i + batchSize, totalAptSalesList.size());
                List<AptSales> batch = totalAptSalesList.subList(i, end);
                
                aptSalesRepository.saveAll(batch);
                totalSaved += batch.size();
                
                log.info("Batch saved {} records. Progress: {}/{}", 
                    batch.size(), totalSaved, totalAptSalesList.size());
            }
            
            log.info("All data saved successfully. Total records saved: {}", totalSaved);
            
        } catch (Exception e) {
            log.error("Error during batch save: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void saveAptTradeDataByPeriod(int startYear, int startMonth) {
        LocalDate startDate = LocalDate.of(startYear, startMonth, 1);
        LocalDate currentDate = LocalDate.now();
        
        log.info("Starting to fetch and save apartment trade data from {}/{} to current date", 
                startYear, String.format("%02d", startMonth));
        
        List<RegionCode> regionCodes = regionCodeRepository.findAll();
        int totalRegions = regionCodes.size();
        log.info("Total {} regions found", totalRegions);
        
        // 전체 데이터를 담을 리스트
        List<AptSales> totalAptSalesList = new ArrayList<>();
        int totalFetched = 0;
        
        // 1. 모든 년월에 대해 순회
        LocalDate iterDate = startDate;
        while (!iterDate.isAfter(currentDate)) {
            String dealYmd = iterDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
            log.info("Processing year/month: {}", dealYmd);
            
            // 2. 각 년월별로 모든 지역 데이터 수집
            for (RegionCode regionCode : regionCodes) {
                String lawdCd = String.format("%05d", regionCode.getId().getGuCode());
                int pageNo = 1;
                int numOfRows = 1000;
                
                while (true) {
                    try {
                        AptTradeResponse response = aptTradeFeignClient.getAptTradeDetail(
                                serviceKey, pageNo, numOfRows, lawdCd, dealYmd);
                        
                        if (response.getBody().getItems() == null || 
                            response.getBody().getItems().getItemList() == null || 
                            response.getBody().getItems().getItemList().isEmpty()) {
                            break;
                        }

                        List<AptSales> currentPageItems = response.getBody().getItems().getItemList().stream()
                            .map(item -> AptSales.from(item, lawdCd))
                            .collect(Collectors.toList());
                        
                        totalAptSalesList.addAll(currentPageItems);
                        totalFetched += currentPageItems.size();
                        
                        // 2000개가 모이면 중간 저장
                        if (totalAptSalesList.size() >= 2000) {
                            log.info("Intermediate saving of {} records (Total fetched: {})", 
                                    totalAptSalesList.size(), totalFetched);
                            aptSalesRepository.saveAll(totalAptSalesList);
                            totalAptSalesList.clear();
                        }

                        int totalCount = Integer.parseInt(response.getBody().getTotalCount());
                        if (pageNo * numOfRows >= totalCount) {
                            break;
                        }
                        pageNo++;
                        
                    } catch (Exception e) {
                        log.error("Error fetching data for region {} in {}: {}", 
                                lawdCd, dealYmd, e.getMessage());
                        break;
                    }
                }
            }
            
            // 다음 월로 이동
            iterDate = iterDate.plusMonths(1);
        }
        
        // 남은 데이터 저장
        if (!totalAptSalesList.isEmpty()) {
            log.info("Saving remaining {} records. Total records fetched: {}", 
                    totalAptSalesList.size(), totalFetched);
            aptSalesRepository.saveAll(totalAptSalesList);
            totalAptSalesList.clear();
        }
        
        log.info("Completed saving historical apartment trade data. Total records fetched: {}", 
                totalFetched);
    }

    @Override
    @Transactional
    public void convertToAptDealSales() {
        log.info("Starting conversion from AptSales to AptDealSales");
        
        aptDealSalesRepository.deleteAll();
        log.info("Cleared existing AptDealSales data");
        
        List<AptDealSales> dealSalesList = new ArrayList<>();
        int processedCount = 0;
        int batchSize = 2000;
        
        try (Stream<AptSalesProjection> stream = aptSalesRepository.findAptSalesForConversion()) {
            Iterator<AptSalesProjection> iterator = stream.iterator();
            
            while (iterator.hasNext()) {
                dealSalesList.add(AptDealSales.from(iterator.next()));
                processedCount++;
                
                if (dealSalesList.size() >= batchSize) {
                    aptDealSalesRepository.saveAll(dealSalesList);
                    log.info("Saved batch of {} records. Total processed: {}", 
                            dealSalesList.size(), processedCount);
                    dealSalesList.clear();
                }
            }
            
            if (!dealSalesList.isEmpty()) {
                aptDealSalesRepository.saveAll(dealSalesList);
                log.info("Saved remaining {} records. Total processed: {}", 
                        dealSalesList.size(), processedCount);
                dealSalesList.clear();
            }
        }
        
        log.info("Completed conversion to AptDealSales. Total records processed: {}", 
                processedCount);
    }

}