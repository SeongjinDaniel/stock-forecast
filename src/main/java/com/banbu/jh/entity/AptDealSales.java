package com.banbu.jh.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.banbu.jh.dto.AptSalesProjection;


@Entity
@Table(name="apt_deal_sales",
    indexes={
        @Index(name="idx_apt_deal_sales2",
            columnList="regional_code,dong,jibun,apartment_name,area_str"),
        @Index(name="idx_deal_date",
            columnList="deal_date"),
        @Index(name="idx_curr_deal_amount",
            columnList="curr_deal_amount"),
        @Index(name="idx_apt_deal_sales_regional_code",
            columnList="regional_code"),
        @Index(name="idx_apt_deal_sales_seq_area_date",
            columnList="apt_seq,area_str,deal_date")
    })
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class AptDealSales {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",columnDefinition="INT UNSIGNED NOT NULL AUTO_INCREMENT")
    private Long id;

    @Column(name="regional_code",nullable=false)
    private Integer regionalCode;

    @Column(name="dong",length=30,nullable=false)
    private String dong;

    @Column(name="jibun",length=20,columnDefinition="VARCHAR(20) DEFAULT ''")
    private String jibun;

    @Column(name="apartment_name",length=80,nullable=false)
    private String apartmentName;

    @Column(name="area",nullable=false,columnDefinition="FLOAT NOT NULL DEFAULT '0'")
    private Float area;

    @Column(name="area_str",length=8)
    private String areaStr;

    @Column(name="max_deal_amount",columnDefinition="INT DEFAULT '0'")
    private Integer maxDealAmount;

    @Column(name="curr_deal_amount",columnDefinition="INT DEFAULT '0'")
    private Integer currDealAmount;

    @Column(name="percent",columnDefinition="FLOAT DEFAULT '0'")
    private Float percent;

    @Column(name="deal_date")
    private LocalDate dealDate;

    @Column(name="build_year",columnDefinition="INT UNSIGNED")
    private Integer buildYear;

    @Column(name="trade_type")
    private Integer tradeType;

    @Column(name="road_name",length=150)
    private String roadName;

    @Column(name="road_name_bonbun_bubun",length=30)
    private String roadNameBonbunBubun;

    @Column(name="apt_seq",length=11)
    private String aptSeq;

    @Builder
    public AptDealSales(String aptSeq, String areaStr, Integer regionalCode, 
                       String dong, String jibun, String apartmentName, 
                       Float area, Integer maxDealAmount, Integer currDealAmount,
                       Float percent, LocalDate dealDate, Integer buildYear,
                       Integer tradeType, String roadName, String roadNameBonbunBubun) {
        this.aptSeq = aptSeq;
        this.areaStr = areaStr;
        this.regionalCode = regionalCode;
        this.dong = dong;
        this.jibun = jibun;
        this.apartmentName = apartmentName;
        this.area = area;
        this.maxDealAmount = maxDealAmount;
        this.currDealAmount = currDealAmount;
        this.percent = percent;
        this.dealDate = dealDate;
        this.buildYear = buildYear;
        this.tradeType = tradeType;
        this.roadName = roadName;
        this.roadNameBonbunBubun = roadNameBonbunBubun;
    }

    public static AptDealSales from(AptSalesProjection projection) {
        float percent = calculatePriceChangePercent(projection.getMaxDealAmount(), projection.getCurrDealAmount());
        
        return builder()
                .aptSeq(projection.getAptSeq())
                .areaStr(projection.getAreaStr())
                .area(projection.getArea())
                .regionalCode(projection.getRegionalCode())
                .dong(projection.getDong())
                .jibun(projection.getJibun())
                .apartmentName(projection.getApartmentName())
                .maxDealAmount(projection.getMaxDealAmount())
                .currDealAmount(projection.getCurrDealAmount())
                .percent(percent)
                .dealDate(projection.getDealDate())
                .buildYear(projection.getBuildYear())
                .tradeType(projection.getTradeType())
                .roadName(projection.getRoadName())
                .roadNameBonbunBubun(projection.getRoadNameBonbunBubun())
                .build();
    }

    private static float calculatePriceChangePercent(Integer maxAmount, Integer currentAmount) {
        if (maxAmount != null && currentAmount != null && maxAmount > 0) {
            return ((float) currentAmount / maxAmount - 1) * 100;
        }
        return 0f;
    }
}