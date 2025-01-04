package com.banbu.jh.service.impl;

import com.banbu.jh.dto.AptSearchCondition;
import com.banbu.jh.entity.AptDealSales;
import com.banbu.jh.repository.AptDealSalesRepository;
import com.banbu.jh.service.BanbuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.banbu.jh.service.RegionCacheService;
import com.banbu.jh.entity.RegionDong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BanbuServiceImpl implements BanbuService {
    private final AptDealSalesRepository aptDealSalesRepository;
    private final RegionCacheService regionCacheService;

    @Override
    public Page<AptDealSales> searchAptDeals(AptSearchCondition condition, Pageable pageable) {
        if (condition.getSi() == null || condition.getSi().isEmpty()) {
            throw new IllegalArgumentException("City(si) parameter is required");
        }

        if (condition.getGu() != null && !condition.getGu().isEmpty()) {
            List<RegionDong> dongList = regionCacheService.getDongListByCityAndGu(condition.getSi(), condition.getGu());
            if (dongList.isEmpty()) {
                throw new IllegalArgumentException("Invalid region: " + condition.getSi() + " " + condition.getGu());
            }
        }

        LocalDate startDate = calculateStartDate(condition.getPeriod());

        return aptDealSalesRepository.searchByCondition(
                condition.getSi(),
                condition.getGu(),
                condition.getDong(),
                condition.getAptName(),
                condition.getTradeType(),
                condition.getMinAmount(),
                condition.getMaxAmount(),
                startDate,
                condition.getBuildYear(),
                pageable
        );
    }

    private LocalDate calculateStartDate(String period) {
        if (period == null || period.equals("ALL")) {
            return null;
        }

        LocalDate now = LocalDate.now();
        return switch (period) {
            case "1W" -> now.minusWeeks(1);
            case "1M" -> now.minusMonths(1);
            case "1Y" -> now.minusYears(1);
            case "3Y" -> now.minusYears(3);
            default -> null;
        };
    }
} 