package com.d_to_a.backend.controller;

import com.d_to_a.backend.dto.*;
import com.d_to_a.backend.entity.User;
import com.d_to_a.backend.repository.UserRepository;
import com.d_to_a.backend.service.AwardService;
import com.d_to_a.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AwardService awardService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterRequest request) {
        UserRegisterResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    // 유저 전체 조회 → /users/all
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> response = users.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // 유저 삭제 → /users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("삭제 성공");
    }

    @PatchMapping("/{userId}/club")
    public ResponseEntity<String> updateUserClub(@PathVariable Long userId,
                                                 @RequestBody UserClubUpdateRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUserClub(userId, request.getClubId(), userDetails.getUsername());
        return ResponseEntity.ok("동아리 정보가 수정되었습니다.");
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<String> changePassword(@PathVariable Long userId,
                                                 @RequestBody UserPasswordChangeRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        userService.changePassword(userId, request.getOldPassword(), request.getNewPassword(), userDetails.getUsername());
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    @PatchMapping("/{userId}/info")
    public ResponseEntity<String> updateUserInfo(@PathVariable Long userId,
                                                 @RequestBody UserInfoUpdateRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUserInfo(userId, request, userDetails.getUsername());
        return ResponseEntity.ok("유저 정보가 성공적으로 수정되었습니다.");
    }

    // 수상 내역 조회
    @GetMapping("/{userId}/awards")
    public ResponseEntity<List<AwardResponse>> getUserAwards(@PathVariable Long userId) {
        return ResponseEntity.ok(awardService.getUserAwards(userId));
    }

    // 수상 내역 추가
    @PostMapping("/{userId}/awards")
    public ResponseEntity<AwardResponse> addAward(@PathVariable Long userId,
                                                  @RequestBody AwardRequest request) {
        return ResponseEntity.ok(awardService.addAward(userId, request));
    }

    // 수상 내역 삭제
    @DeleteMapping("/awards/{awardId}")
    public ResponseEntity<Void> deleteAward(@PathVariable Long awardId) {
        awardService.deleteAward(awardId);
        return ResponseEntity.noContent().build();
    }

}
