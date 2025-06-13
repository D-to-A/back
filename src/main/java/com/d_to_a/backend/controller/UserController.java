package com.d_to_a.backend.controller;

import com.d_to_a.backend.dto.*;
import com.d_to_a.backend.entity.User;
import com.d_to_a.backend.repository.UserRepository;
import com.d_to_a.backend.service.AwardService;
import com.d_to_a.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@Tag(name = "마이페이지", description = "유저 정보 및 활동 관리 API")
@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AwardService awardService;

    @Operation(summary = "유저 전체 조회", description = "모든 유저의 정보를 조회합니다.")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> response = users.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 삭제", description = "ID로 유저를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("삭제 성공");
    }

    @Operation(summary = "소속 동아리 변경", description = "유저의 소속 동아리를 변경합니다.")
    @PatchMapping("/{userId}/club")
    public ResponseEntity<String> updateUserClub(@PathVariable Long userId,
                                                 @RequestBody UserClubUpdateRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUserClub(userId, request.getClubId(), userDetails.getUsername());
        return ResponseEntity.ok("동아리 정보가 수정되었습니다.");
    }

    @Operation(summary = "비밀번호 변경", description = "유저 비밀번호를 변경합니다.")
    @PatchMapping("/{userId}/password")
    public ResponseEntity<String> changePassword(@PathVariable Long userId,
                                                 @RequestBody UserPasswordChangeRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        userService.changePassword(userId, request.getOldPassword(), request.getNewPassword(), userDetails.getUsername());
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    @Operation(summary = "유저 정보 수정", description = "유저의 이름, 학번, 기술스택 등의 정보를 수정합니다.")
    @PatchMapping("/{userId}/info")
    public ResponseEntity<String> updateUserInfo(@PathVariable Long userId,
                                                 @RequestBody UserInfoUpdateRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUserInfo(userId, request, userDetails.getUsername());
        return ResponseEntity.ok("유저 정보가 성공적으로 수정되었습니다.");
    }

    @Operation(summary = "수상 이력 조회", description = "유저 ID로 수상 이력을 조회합니다.")
    @GetMapping("/{userId}/awards")
    public ResponseEntity<List<AwardResponse>> getUserAwards(@PathVariable Long userId) {
        return ResponseEntity.ok(awardService.getUserAwards(userId));
    }

    @Operation(summary = "수상 이력 추가", description = "특정 유저의 수상 내역을 추가합니다.")
    @PostMapping("/{userId}/awards")
    public ResponseEntity<AwardResponse> addAward(@PathVariable Long userId,
                                                  @RequestBody AwardRequest request) {
        return ResponseEntity.ok(awardService.addAward(userId, request));
    }

    @Operation(summary = "수상 이력 삭제", description = "수상 이력 ID로 해당 내역을 삭제합니다.")
    @DeleteMapping("/awards/{awardId}")
    public ResponseEntity<Void> deleteAward(@PathVariable Long awardId) {
        awardService.deleteAward(awardId);
        return ResponseEntity.noContent().build();
    }
}
