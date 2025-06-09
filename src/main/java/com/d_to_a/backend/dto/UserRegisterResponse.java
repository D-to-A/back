package com.d_to_a.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRegisterResponse {
    private Long id;
    private String username;
    private String realName;
    private String grade;
    private String className;
    private String studentNum;
    private String mainStack;
    private String subStack;
    private String role;
    private String clubName;
}
