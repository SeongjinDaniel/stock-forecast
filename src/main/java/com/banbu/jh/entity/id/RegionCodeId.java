package com.banbu.jh.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegionCodeId implements Serializable {
    
    @Column(name = "city_code")
    private Integer cityCode;
    
    @Column(name = "gu_code")
    private Integer guCode;
} 