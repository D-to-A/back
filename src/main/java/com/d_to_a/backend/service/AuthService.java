package com.d_to_a.backend.service;

import com.d_to_a.backend.dto.*;
import com.d_to_a.backend.entity.Club;
import com.d_to_a.backend.entity.User;
import com.d_to_a.backend.repository.ClubRepository;
import com.d_to_a.backend.repository.UserRepository;
import com.d_to_a.backend.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JdbcTemplate jdbcTemplate;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        user.setRefreshToken(refreshToken);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(String refreshToken) {
        User user = (User) userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

        user.setRefreshToken(null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

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
}
