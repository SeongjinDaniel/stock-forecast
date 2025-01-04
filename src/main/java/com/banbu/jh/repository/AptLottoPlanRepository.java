package com.banbu.jh.repository;

import com.banbu.jh.entity.AptLottoPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AptLottoPlanRepository extends JpaRepository<AptLottoPlan, Long> {
    
    // 지역별 청약 정보 조회
    List<AptLottoPlan> findByRegion(String region);
    
    // 특정 기간 내의 청약 정보 조회
    List<AptLottoPlan> findByReciptBeginBetweenOrReciptEndBetween(
            LocalDate startDate1, LocalDate endDate1,
            LocalDate startDate2, LocalDate endDate2);
    
    // 지역별, 기간별 청약 정보 조회
    List<AptLottoPlan> findByRegionAndReciptBeginGreaterThanEqualAndReciptEndLessThanEqual(
            String region, LocalDate startDate, LocalDate endDate);
    
    // 아파트명으로 청약 정보 검색
    List<AptLottoPlan> findByHouseNmContaining(String houseNm);
    
    // 공급 세대수 범위로 검색
    List<AptLottoPlan> findByTotalSupplyBetween(Integer minSupply, Integer maxSupply);
    
    // 현재 청약 가능한 물량 조회 (현재 날짜가 청약 기간에 포함되는 경우)
    @Query("SELECT a FROM AptLottoPlan a WHERE :currentDate BETWEEN a.reciptBegin AND a.reciptEnd")
    List<AptLottoPlan> findCurrentAvailablePlans(@Param("currentDate") LocalDate currentDate);
    
    // 지역별 청약 예정 물량 조회 (청약 시작일이 현재보다 이후인 경우)
    List<AptLottoPlan> findByRegionAndReciptBeginAfter(String region, LocalDate currentDate);
    
    // 특정 타입의 청약 정보 조회
    List<AptLottoPlan> findByType(String type);

    List<AptLottoPlan> findAllByRegion(String region);
}