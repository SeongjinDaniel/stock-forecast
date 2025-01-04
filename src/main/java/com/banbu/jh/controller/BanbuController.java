package com.banbu.jh.controller;

import com.banbu.jh.service.RegionCacheService;
import com.banbu.jh.entity.RegionDong;
import com.banbu.jh.dto.AptSearchCondition;
import com.banbu.jh.entity.AptDealSales;
import com.banbu.jh.entity.AptLottoPlan;
import com.banbu.jh.service.BanbuService;
import com.banbu.jh.service.AptLottoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/banbu")
public class BanbuController {

    private final RegionCacheService regionCacheService;
    private final BanbuService banbuService;
    private final AptLottoService aptLottoService;

    @GetMapping("/regions")
    public ResponseEntity<Map<String, Object>> getRegions() {
        Map<String, Object> response = new HashMap<>();
        response.put("cities", regionCacheService.getAllCities());
        response.put("totalCount", regionCacheService.getAllRegions().size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/regions/cities")
    public ResponseEntity<List<String>> getCities() {
        return ResponseEntity.ok(regionCacheService.getAllCities());
    }

    @GetMapping("/regions/{city}/gus")
    public ResponseEntity<List<String>> getGuList(@PathVariable String city) {
        return ResponseEntity.ok(regionCacheService.getGuListByCity(city));
    }

    @GetMapping("/regions/{city}/{gu}/dongs")
    public ResponseEntity<List<RegionDong>> getDongList(
            @PathVariable String city,
            @PathVariable String gu) {
        return ResponseEntity.ok(regionCacheService.getDongListByCityAndGu(city, gu));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AptDealSales>> searchAptDeals(
            @RequestParam String si,
            @RequestParam(required = false) String gu,
            @RequestParam(required = false) String dong,
            @RequestParam(required = false) String aptName,
            @RequestParam(required = false) Integer tradeType,
            @RequestParam(required = false) Integer minAmount,
            @RequestParam(required = false) Integer maxAmount,
            @RequestParam(defaultValue = "ALL") String period,
            @RequestParam(required = false) Integer buildYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        AptSearchCondition condition = AptSearchCondition.builder()
                .si(si)
                .gu(gu)
                .dong(dong)
                .aptName(aptName)
                .tradeType(tradeType)
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .period(period)
                .buildYear(buildYear)
                .build();

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "percent"));
        return ResponseEntity.ok(banbuService.searchAptDeals(condition, pageRequest));
    }

    @PostMapping("/apt-lotto/collect")
    public ResponseEntity<String> collectAptLottoData() {
        try {
            aptLottoService.collectAndSaveAptLottoData();
            return ResponseEntity.ok("Apt lotto data collection completed successfully");
        } catch (Exception e) {
            log.error("Error collecting apt lotto data: ", e);
            return ResponseEntity.internalServerError()
                    .body("Error collecting apt lotto data: " + e.getMessage());
        }
    }

    @GetMapping("/apt-lotto/latest")
    public ResponseEntity<List<AptLottoPlan>> getLatestAptLottoByRegion(
            @RequestParam String region) {
        List<AptLottoPlan> results = aptLottoService.findLatestByRegion(region);
        return ResponseEntity.ok(results);
    }
}