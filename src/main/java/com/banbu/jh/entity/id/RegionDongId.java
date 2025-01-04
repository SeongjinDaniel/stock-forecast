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
public class RegionDongId implements Serializable {
    
    @Column(name = "gu_code")
    private Integer guCode;
    
    @Column(name = "dong", length = 20)
    private String dong;
}