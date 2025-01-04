package com.banbu.jh.controller;

import com.banbu.jh.dto.AptTradeResponse;
import com.banbu.jh.service.AptTradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/apt-trade")
public class AptTradeController {

    private final AptTradeService aptTradeService;

    @GetMapping("/sales")
    public AptTradeResponse testAptTrade(
            @RequestParam int pageNo,
            @RequestParam int numOfRows,
            @RequestParam String lawdCd,
            @RequestParam String dealYmd) {
        return aptTradeService.getAptTradeDetail(pageNo, numOfRows, lawdCd, dealYmd);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveAptTrade(
            @RequestParam String lawdCd,
            @RequestParam String dealYmd) {
        aptTradeService.saveAptTradeData(lawdCd, dealYmd);
        return ResponseEntity.ok("Data saved successfully");
    }

    @PostMapping("/save/all")
    public ResponseEntity<String> saveAllRegionAptTrade(@RequestParam String dealYmd) {
        aptTradeService.saveAllRegionAptTradeData(dealYmd);
        return ResponseEntity.ok("All region data saved successfully");
    }

    @PostMapping("/collect/period")
    public ResponseEntity<String> collectAptTradeDataByPeriod(
            @RequestParam int startYear,
            @RequestParam int startMonth) {
        aptTradeService.saveAptTradeDataByPeriod(startYear, startMonth);
        return ResponseEntity.ok("Data collection completed successfully");
    }

    @PostMapping("/convert/deal-sales")
    public ResponseEntity<String> convertToAptDealSales() {
        try {
            aptTradeService.convertToAptDealSales();
            return ResponseEntity.ok("Conversion completed successfully");
        } catch (Exception e) {
            log.error("Error during conversion: ", e);
            return ResponseEntity.internalServerError()
                    .body("Error during conversion: " + e.getMessage());
        }
    }

}