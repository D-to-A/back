package com.d_to_a.backend.repository;

import com.d_to_a.backend.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
}
