package com.banbu.jh.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.banbu.jh.entity.AptDealSales;

@Repository
public interface AptDealSalesRepository extends JpaRepository<AptDealSales, Long> {
    @Query("""
        SELECT a FROM AptDealSales a
        WHERE (:si IS NOT NULL)
        AND (:gu IS NULL OR a.regionalCode IN 
            (SELECT r.id.guCode FROM RegionCode r WHERE r.city = :si AND r.gu = :gu))
        AND (:dong IS NULL OR a.dong = :dong)
        AND (:aptName IS NULL OR a.apartmentName LIKE %:aptName%)
        AND (:tradeType IS NULL OR a.tradeType = :tradeType)
        AND (:minAmount IS NULL OR a.currDealAmount >= :minAmount)
        AND (:maxAmount IS NULL OR a.currDealAmount <= :maxAmount)
        AND (:startDate IS NULL OR a.dealDate >= :startDate)
        AND (:buildYear IS NULL OR a.buildYear >= :buildYear)
        """)
    Page<AptDealSales> searchByCondition(
            String si,
            String gu,
            String dong,
            String aptName,
            Integer tradeType,
            Integer minAmount,
            Integer maxAmount,
            LocalDate startDate,
            Integer buildYear,
            Pageable pageable);
}