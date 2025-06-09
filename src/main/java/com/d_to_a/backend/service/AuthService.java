package com.d_to_a.backend.service;

import com.d_to_a.backend.dto.LoginRequest;
import com.d_to_a.backend.dto.LoginResponse;
import com.d_to_a.backend.entity.User;
import com.d_to_a.backend.repository.UserRepository;
import com.d_to_a.backend.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        // 1. 유저 존재 확인
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 비밀번호 일치 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 토큰 발급
        String accessToken = jwtTokenProvider.generateAccessToken(String.valueOf(user));
        String refreshToken = jwtTokenProvider.generateRefreshToken(String.valueOf(user));

        // 4. 리프레시 토큰 저장 및 updatedAt 갱신
        user.setRefreshToken(refreshToken);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // 5. 응답 반환
        return LoginResponse.builder()
                .userId(user.getId())               // userId 추가
                .username(user.getUsername())
                .realName(user.getRealName())       // 이름도 응답에 포함
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
