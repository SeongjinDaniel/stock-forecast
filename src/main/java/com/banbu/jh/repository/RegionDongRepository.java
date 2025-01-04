package com.banbu.jh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.banbu.jh.entity.RegionDong;
import com.banbu.jh.entity.id.RegionDongId;
import java.util.List;

@Repository
public interface RegionDongRepository extends JpaRepository<RegionDong, RegionDongId> {
    List<RegionDong> findByIdGuCode(Integer guCode);
    List<RegionDong> findByCityAndGu(String city, String gu);
} 