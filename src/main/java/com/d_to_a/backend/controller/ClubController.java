package com.d_to_a.backend.controller;

import com.d_to_a.backend.entity.Club;
import com.d_to_a.backend.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "동아리", description = "동아리 생성, 조회, 삭제 API")
@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @Operation(summary = "동아리 생성", description = "이름, 기술스택, 설명을 입력해 새로운 동아리를 생성합니다.")
    @PostMapping
    public ResponseEntity<Club> createClub(@RequestParam String name,
                                           @RequestParam String stack,
                                           @RequestParam String description) {
        return ResponseEntity.ok(clubService.createClub(name, stack, description));
    }

    @Operation(summary = "전체 동아리 조회", description = "등록된 모든 동아리 정보를 반환합니다.")
    @GetMapping("/all")
    public ResponseEntity<List<Club>> getAll() {
        return ResponseEntity.ok(clubService.findAll());
    }

    @Operation(summary = "동아리 삭제", description = "ID를 기준으로 해당 동아리를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clubService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}