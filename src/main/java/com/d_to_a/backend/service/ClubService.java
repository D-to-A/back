package com.d_to_a.backend.service;

import com.d_to_a.backend.entity.Club;
import com.d_to_a.backend.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final JdbcTemplate jdbcTemplate;

    // 1. 동아리 생성
    public Club createClub(String name, String stack, String description) {
        Club club = Club.builder()
                .name(name)
                .clubStack(stack)
                .description(description)
                .build();

        Club saved = clubRepository.save(club);

        Long maxClubId = jdbcTemplate.queryForObject("SELECT IFNULL(MAX(id), 0) FROM clubs", Long.class);
        jdbcTemplate.execute("ALTER TABLE clubs AUTO_INCREMENT = " + (maxClubId + 1));

        // AUTO_INCREMENT 조정
        long count = clubRepository.count();
        jdbcTemplate.execute("ALTER TABLE clubs AUTO_INCREMENT = " + (count + 1));

        return saved;
    }

    // 2. 동아리 전체 조회
    public List<Club> findAll() {
        return clubRepository.findAll();
    }

    // 3. 동아리 단건 조회
    public Optional<Club> findById(Long id) {
        return clubRepository.findById(id);
    }

    // 4. 동아리 삭제
    public void deleteById(Long id) {
        clubRepository.deleteById(id);

        // 삭제 후 AUTO_INCREMENT 초기화
        Long maxId = jdbcTemplate.queryForObject("SELECT IFNULL(MAX(id), 0) FROM clubs", Long.class);
        Long currentAI = jdbcTemplate.queryForObject(
                "SELECT AUTO_INCREMENT FROM information_schema.TABLES " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'clubs'", Long.class);

        if (currentAI != null && maxId != null && currentAI > maxId + 1) {
            jdbcTemplate.execute("ALTER TABLE clubs AUTO_INCREMENT = " + (maxId + 1));
        }
    }


}
