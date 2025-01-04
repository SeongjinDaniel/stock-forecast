package com.banbu.jh.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;
import java.math.BigDecimal;
import com.banbu.jh.entity.id.RegionCodeId;

@Entity
@Table(name = "region_code")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegionCode {
    
    @EmbeddedId
    private RegionCodeId id;
    
    @Column(name = "city", length = 10, nullable = false)
    private String city;
    
    @Column(name = "gu", length = 20, nullable = false)
    private String gu;
    
    @Column(name = "area", precision = 10, scale = 2)
    private BigDecimal area;
} 