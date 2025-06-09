package com.d_to_a.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoUpdateRequest {
    private String realName;
    private String grade;
    private String className;
    private String studentNum;
    private String mainStack;
    private String subStack;
}
