package com.banbu.jh.repository;

import com.banbu.jh.entity.RegionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RegionCodeRepository extends JpaRepository<RegionCode, Long> {
    @Query("SELECT r FROM RegionCode r WHERE r.city = :city AND r.gu = :gu")
    Optional<RegionCode> findByCityAndGu(@Param("city") String city, @Param("gu") String gu);

    @Query("SELECT r FROM RegionCode r WHERE r.id.guCode = :guCode")
    Optional<RegionCode> findByIdGuCode(@Param("guCode") Integer guCode);
} 