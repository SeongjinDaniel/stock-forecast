package com.banbu.jh.service;

import java.util.List;

import com.banbu.jh.entity.AptLottoPlan;

public interface AptLottoService {
    void collectAndSaveAptLottoData();
    List<AptLottoPlan> findLatestByRegion(String region);
} 