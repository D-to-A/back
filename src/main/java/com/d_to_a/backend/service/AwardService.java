package com.d_to_a.backend.service;

import com.d_to_a.backend.dto.AwardRequest;
import com.d_to_a.backend.dto.AwardResponse;
import com.d_to_a.backend.entity.Award;
import com.d_to_a.backend.entity.User;
import com.d_to_a.backend.repository.AwardRepository;
import com.d_to_a.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final AwardRepository awardRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    public List<AwardResponse> getUserAwards(Long userId) {
        List<Award> awards = awardRepository.findByUserId(userId);
        return awards.stream()
                .map(award -> AwardResponse.builder()
                        .id(award.getId())
                        .title(award.getTitle())
                        .description(award.getDescription())
                        .awardedAt(award.getAwardedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public AwardResponse addAward(Long userId, AwardRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        if (request.getAwardedAt() == null) {
            throw new IllegalArgumentException("수상 날짜(awardedAt)는 필수 항목입니다.");
        }

        Award award = Award.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .awardedAt(request.getAwardedAt())
                .user(user)
                .build();

        Award saved = awardRepository.save(award);
        resetAutoIncrement();  // 삽입 후 AUTO_INCREMENT 초기화

        return AwardResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .awardedAt(saved.getAwardedAt())
                .build();
    }

    public void deleteAward(Long awardId) {
        awardRepository.deleteById(awardId);
        resetAutoIncrement();  // 삭제 후 AUTO_INCREMENT 초기화
    }

    private void resetAutoIncrement() {
        Long maxId = jdbcTemplate.queryForObject("SELECT IFNULL(MAX(id), 0) FROM awards", Long.class);
        long nextId = (maxId != null && maxId > 0) ? maxId + 1 : 1;

        // 항상 AUTO_INCREMENT 값을 강제 설정
        jdbcTemplate.execute("ALTER TABLE awards AUTO_INCREMENT = " + nextId);
        System.out.println("[AUTO_INCREMENT] awards 테이블 다음 ID 설정 완료 → " + nextId);
    }
}
