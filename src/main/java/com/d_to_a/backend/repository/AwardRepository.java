package com.d_to_a.backend.repository;

import com.d_to_a.backend.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardRepository extends JpaRepository<Award, Long> {
    List<Award> findByUserId(Long userId);
}
