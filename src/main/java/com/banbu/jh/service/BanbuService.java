package com.banbu.jh.service;

import com.banbu.jh.dto.AptSearchCondition;
import com.banbu.jh.entity.AptDealSales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BanbuService {
    Page<AptDealSales> searchAptDeals(AptSearchCondition condition, Pageable pageable);
} 