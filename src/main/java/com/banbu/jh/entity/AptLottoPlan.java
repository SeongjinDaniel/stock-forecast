package com.banbu.jh.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apt_lotto_plan",
    indexes = {
        @Index(name = "idx_apt_lotto_plan1", 
            columnList = "region,recipt_begin,recipt_end")
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AptLottoPlan {
    
    @Id
    @Column(name = "id", columnDefinition = "INT UNSIGNED NOT NULL")
    private Long id;
    
    @Column(name = "region", length = 10, nullable = false)
    private String region;
    
    @Column(name = "recipt_begin")
    private LocalDate reciptBegin;
    
    @Column(name = "recipt_end")
    private LocalDate reciptEnd;
    
    @Column(name = "house_nm", length = 80, nullable = false)
    private String houseNm;
    
    @Column(name = "type", length = 10, nullable = false)
    private String type;
    
    @Column(name = "url", length = 300, nullable = false)
    private String url;
    
    @Column(name = "business_name", length = 50)
    private String businessName;
    
    @Column(name = "builder_name", length = 50)
    private String builderName;
    
    @Column(name = "total_supply", columnDefinition = "INT UNSIGNED")
    private Integer totalSupply;
    
    @Column(name = "address", length = 150)
    private String address;
    
    @Builder
    public AptLottoPlan(Long id, String region, LocalDate reciptBegin, 
                       LocalDate reciptEnd, String houseNm, String type, 
                       String url, String businessName, String builderName, 
                       Integer totalSupply, String address) {
        this.id = id;
        this.region = region;
        this.reciptBegin = reciptBegin;
        this.reciptEnd = reciptEnd;
        this.houseNm = houseNm;
        this.type = type;
        this.url = url;
        this.businessName = businessName;
        this.builderName = builderName;
        this.totalSupply = totalSupply;
        this.address = address;
    }
} 