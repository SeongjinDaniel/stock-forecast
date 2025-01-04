package com.banbu.jh.entity;

import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import com.banbu.jh.dto.AptTradeResponse;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AptSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED NOT NULL AUTO_INCREMENT")
    private Long id;  // 'INT UNSIGNED' 범위를 고려하여 Long 타입 권장

    @Column(name = "regional_code", nullable = false)
    private Integer regionalCode;  // NOT NULL

    @Column(name = "dong", length = 30)
    private String dong;

    @Column(name = "jibun",
            length = 20,
            columnDefinition = "VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT ''")
    private String jibun;

    @Column(name = "apartment_name", length = 80)
    private String apartmentName;

    @Column(name = "area_str", length = 8)
    private String areaStr;

    @Column(name = "floor", columnDefinition = "INT DEFAULT '1'")
    private Integer floor;

    @Column(name = "deal_amount", columnDefinition = "INT DEFAULT '0'")
    private Integer dealAmount;

    @Column(name = "build_year", columnDefinition = "INT DEFAULT '0'")
    private Integer buildYear;

    @Column(name = "deal_date", nullable = false)
    private LocalDate dealDate;  // DATE 타입 매핑

    @Column(name = "trade_type", columnDefinition = "INT COMMENT '거래구분, 중개거래: 0, 직거래: 1'")
    private Integer tradeType;

    @Column(name = "agency_address",
            length = 150,
            columnDefinition = "VARCHAR(150) DEFAULT NULL COMMENT '중개사 소재지 주소'")
    private String agencyAddress;

    @Column(name = "registration_date",
            length = 8,
            columnDefinition = "VARCHAR(8) DEFAULT NULL COMMENT '등기일자 YY.MM.DD 형식'")
    private String registrationDate;

    @Column(name = "seller",
            length = 20,
            columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '매도자'")
    private String seller;

    @Column(name = "buyer",
            length = 20,
            columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '매수자'")
    private String buyer;

    @Column(name = "apartment_dong",
            length = 50,
            columnDefinition = "VARCHAR(50) DEFAULT NULL COMMENT '아파트 동 번호'")
    private String apartmentDong;

    @Column(name = "apt_seq", length = 11)
    private String aptSeq;

    @Column(name = "road_name", length = 150)
    private String roadName;

    @Column(name = "road_name_bonbun_bubun",
            length = 30,
            columnDefinition = "VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL")
    private String roadNameBonbunBubun;

    public static AptSales from(AptTradeResponse.Item item, String lawdCd) {
        AptSales aptSales = new AptSales();
        aptSales.setRegionalCode(Integer.parseInt(lawdCd));
        aptSales.setDong(item.getUmdNm());
        aptSales.setJibun(item.getJibun());
        aptSales.setApartmentName(item.getAptNm());
        aptSales.setAreaStr(item.getExcluUseAr());
        aptSales.setFloor(Integer.parseInt(item.getFloor()));
        aptSales.setDealAmount(Integer.parseInt(item.getDealAmount().replace(",", "")));
        aptSales.setBuildYear(Integer.parseInt(item.getBuildYear()));
        
        // 거래일자 설정
        LocalDate dealDate = LocalDate.of(
                Integer.parseInt(item.getDealYear()),
                Integer.parseInt(item.getDealMonth()),
                Integer.parseInt(item.getDealDay())
        );
        aptSales.setDealDate(dealDate);
        
        aptSales.setTradeType("직거래".equals(item.getDealingGbn()) ? 1 : 0);
        aptSales.setAgencyAddress(item.getEstateAgentSggNm());
        aptSales.setRegistrationDate(item.getRgstDate());
        aptSales.setSeller(item.getSlerGbn());
        aptSales.setBuyer(item.getBuyerGbn());
        aptSales.setApartmentDong(item.getAptDong());
        aptSales.setAptSeq(item.getAptSeq());
        aptSales.setRoadName(item.getRoadNm());
        aptSales.setRoadNameBonbunBubun(item.getRoadNmBonbun() + "-" + item.getRoadNmBubun());
        
        return aptSales;
    }
}