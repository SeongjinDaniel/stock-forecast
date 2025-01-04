package com.banbu.jh.repository;

import com.banbu.jh.entity.AptSales;
import com.banbu.jh.dto.AptSalesProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;


@Repository
public interface AptSalesRepository extends JpaRepository<AptSales, Long> {
    @Query(value = """
            SELECT
                a.apt_seq AS aptSeq,
                a.area_str AS areaStr,
                a.area AS area,
                a.regional_code AS regionalCode,
                a.dong AS dong,
                a.jibun AS jibun,
                a.apartment_name AS apartmentName,
                a.deal_date AS dealDate,
                a.deal_amount AS currDealAmount,
                a.build_year AS buildYear,
                a.trade_type AS tradeType,
                a.road_name AS roadName,
                a.road_name_bonbun_bubun AS roadNameBonbunBubun,
                (SELECT MAX(x.deal_amount)
                 FROM apt_sales x
                 WHERE x.apt_seq = a.apt_seq 
                 AND x.area_str = a.area_str
                ) AS maxDealAmount
            FROM apt_sales a
            JOIN (
                SELECT apt_seq, area_str, MAX(deal_date) AS max_deal_date
                FROM apt_sales
                GROUP BY apt_seq, area_str
            ) t
                ON a.apt_seq = t.apt_seq
                AND a.area_str = t.area_str
                AND a.deal_date = t.max_deal_date
            """, nativeQuery = true)
    Stream<AptSalesProjection> findAptSalesForConversion();
}
