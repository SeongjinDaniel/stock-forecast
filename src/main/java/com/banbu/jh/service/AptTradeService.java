package com.banbu.jh.service;

import com.banbu.jh.dto.AptTradeResponse;

public interface AptTradeService {
    AptTradeResponse getAptTradeDetail(int pageNo, int numOfRows, String lawdCd, String dealYmd);
    void saveAptTradeData(String lawdCd, String dealYmd);
    void saveAllRegionAptTradeData(String dealYmd);
    void saveAptTradeDataByPeriod(int startYear, int startMonth);
    void convertToAptDealSales();
}