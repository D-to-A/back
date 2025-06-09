package com.d_to_a.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRegisterRequest {
    private String username;
    private String password;
    private String realName;
    private String grade;
    private String className;
    private String studentNum;
    private String mainStack;
    private String subStack;
    private Long clubId;  // 동아리 ID
}
