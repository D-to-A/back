package com.d_to_a.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private Long userId;
    private String username;
    private String realName;
    private String role;
    private String accessToken;
    private String refreshToken;
}
