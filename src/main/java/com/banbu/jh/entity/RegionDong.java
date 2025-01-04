package com.banbu.jh.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.banbu.jh.entity.id.RegionDongId;

@Entity
@Table(name = "region_dong",
    indexes = {
        @Index(name = "idx_region_dong", columnList = "gu_code"),
        @Index(name = "idx_city_gu_dong", columnList = "city,gu,dong")
    })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegionDong {
    
    @EmbeddedId
    private RegionDongId id;
    
    @Column(name = "city", length = 10, nullable = false)
    private String city;
    
    @Column(name = "gu", length = 20, nullable = false)
    private String gu;
    
    @Column(name = "city_code", nullable = false)
    private Integer cityCode;
}