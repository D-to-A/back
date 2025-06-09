package com.d_to_a.backend.dto;

import com.d_to_a.backend.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
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

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.realName = user.getRealName();
        this.grade = user.getGrade();
        this.className = user.getClassName();
        this.studentNum = user.getStudentNum();
        this.mainStack = user.getMainStack();
        this.subStack = user.getSubStack();
        this.role = user.getRole();
        this.clubName = user.getClub().getName(); // 여기서 Club 정보를 추출
    }
}
