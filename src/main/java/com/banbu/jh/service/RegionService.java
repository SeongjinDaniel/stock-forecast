package com.banbu.jh.service;

import com.banbu.jh.entity.RegionCode;
import com.banbu.jh.entity.RegionDong;
import java.util.List;

public interface RegionService {
    RegionCode getRegionByGuCode(Integer guCode);
    List<RegionDong> getDongsByGuCode(Integer guCode);
}
