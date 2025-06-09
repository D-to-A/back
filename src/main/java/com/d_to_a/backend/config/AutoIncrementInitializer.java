package com.d_to_a.backend.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoIncrementInitializer {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initAutoIncrements() {
        // users 테이블 초기화
        Long userMaxId = jdbcTemplate.queryForObject("SELECT IFNULL(MAX(id), 0) FROM users", Long.class);
        jdbcTemplate.execute("ALTER TABLE users AUTO_INCREMENT = " + (userMaxId + 1));

        // clubs 테이블 초기화
        Long clubMaxId = jdbcTemplate.queryForObject("SELECT IFNULL(MAX(id), 0) FROM clubs", Long.class);
        jdbcTemplate.execute("ALTER TABLE clubs AUTO_INCREMENT = " + (clubMaxId + 1));

        // awards 테이블 초기화
        Long awardsMaxId = jdbcTemplate.queryForObject("SELECT IFNULL(MAX(id), 0) FROM awards", Long.class);
        jdbcTemplate.execute("ALTER TABLE awards AUTO_INCREMENT = " + (awardsMaxId + 1));
    }
}
