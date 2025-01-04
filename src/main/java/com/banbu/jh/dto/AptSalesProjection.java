package com.banbu.jh.dto;

import java.time.LocalDate;

public interface AptSalesProjection {
    String getAptSeq();
    String getAreaStr();
    Integer getRegionalCode();
    String getDong();
    String getJibun();
    String getApartmentName();
    LocalDate getDealDate();
    Integer getCurrDealAmount();
    Integer getBuildYear();
    Integer getTradeType();
    String getRoadName();
    String getRoadNameBonbunBubun();
    Integer getMaxDealAmount();
    Float getArea();
}