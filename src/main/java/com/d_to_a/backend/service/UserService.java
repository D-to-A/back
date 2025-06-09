package com.d_to_a.backend.service;

import com.d_to_a.backend.dto.UserInfoUpdateRequest;
import com.d_to_a.backend.dto.UserRegisterRequest;
import com.d_to_a.backend.dto.UserRegisterResponse;
import com.d_to_a.backend.entity.User;
import com.d_to_a.backend.entity.Club;
import com.d_to_a.backend.repository.UserRepository;
import com.d_to_a.backend.repository.ClubRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public UserRegisterResponse register(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        Club club = clubRepository.findById(request.getClubId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동아리입니다."));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .realName(request.getRealName())
                .grade(request.getGrade())
                .className(request.getClassName())
                .studentNum(request.getStudentNum())
                .mainStack(request.getMainStack())
                .subStack(request.getSubStack())
                .club(club)
                .role("STUDENT")
                .refreshToken("")
                .lastPwdChange(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        // 현재 MAX(id)
        Long maxId = jdbcTemplate.queryForObject("SELECT IFNULL(MAX(id), 0) FROM users", Long.class);

        // 현재 AUTO_INCREMENT 값 조회
        Long currentAutoIncrement = jdbcTemplate.queryForObject(
                "SELECT AUTO_INCREMENT FROM information_schema.TABLES " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users'", Long.class
        );

        // 필요 시만 업데이트
        if (currentAutoIncrement != null && maxId != null && currentAutoIncrement <= maxId) {
            jdbcTemplate.execute("ALTER TABLE users AUTO_INCREMENT = " + (maxId + 1));
        }


        return UserRegisterResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .realName(savedUser.getRealName())
                .grade(savedUser.getGrade())
                .className(savedUser.getClassName())
                .studentNum(savedUser.getStudentNum())
                .mainStack(savedUser.getMainStack())
                .subStack(savedUser.getSubStack())
                .role(savedUser.getRole())
                .clubName(savedUser.getClub().getName())
                .build();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);

        // 삭제 후 AUTO_INCREMENT 초기화
        Long maxId = jdbcTemplate.queryForObject("SELECT IFNULL(MAX(id), 0) FROM users", Long.class);
        Long currentAI = jdbcTemplate.queryForObject(
                "SELECT AUTO_INCREMENT FROM information_schema.TABLES " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users'", Long.class);

        if (currentAI != null && maxId != null && currentAI > maxId + 1) {
            jdbcTemplate.execute("ALTER TABLE users AUTO_INCREMENT = " + (maxId + 1));
        }
    }

    @Transactional
    public void updateUserClub(Long userId, Long clubId, String authenticatedUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 본인 확인
        if (!user.getUsername().equals(authenticatedUsername)) {
            throw new SecurityException("자신의 동아리만 변경할 수 있습니다.");
        }

        Club newClub = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("해당 동아리가 존재하지 않습니다."));

        user.setClub(newClub);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword, String authenticatedUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (!user.getUsername().equals(authenticatedUsername)) {
            throw new SecurityException("본인만 비밀번호를 변경할 수 있습니다.");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastPwdChange(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void updateUserInfo(Long userId, UserInfoUpdateRequest request, String authenticatedUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (!user.getUsername().equals(authenticatedUsername)) {
            throw new SecurityException("본인만 정보를 수정할 수 있습니다.");
        }

        user.setRealName(request.getRealName());
        user.setGrade(request.getGrade());
        user.setClassName(request.getClassName());
        user.setStudentNum(request.getStudentNum());
        user.setMainStack(request.getMainStack());
        user.setSubStack(request.getSubStack());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}
