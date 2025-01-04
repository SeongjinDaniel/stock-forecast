package com.banbu.jh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AptSearchCondition {
    private String si;          // 시
    private String gu;          // 구
    private String dong;        // 동
    private String aptName;     // 아파트명
    private Integer tradeType;  // 거래유형 (0: 부동산거래, 1: 직거래, null: 전체)
    private Integer minAmount;  // 최소 거래금액
    private Integer maxAmount;  // 최대 거래금액
    private String period;      // 거래일자 기간 (1W, 1M, 1Y, 3Y, ALL)
    private Integer buildYear;  // 건축년도
} 