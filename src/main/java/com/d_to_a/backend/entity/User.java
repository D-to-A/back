package com.d_to_a.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "real_name", nullable = false, length = 255)
    private String realName;

    @Column(nullable = true, length = 255)
    private String grade;

    @Column(name = "class_name", nullable = true, length = 255)
    private String className;

    @Column(name = "student_num", nullable = true, length = 255)
    private String studentNum;

    @Column(name = "main_stack", nullable = true, length = 255)
    private String mainStack;

    @Column(name = "sub_stack", nullable = true, length = 255)
    private String subStack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = true)
    private Club club;

    @Column(length = 255)
    private String role;

    @Lob
    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "last_pwd_change")
    private LocalDateTime lastPwdChange;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
