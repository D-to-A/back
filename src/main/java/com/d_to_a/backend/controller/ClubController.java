package com.d_to_a.backend.controller;

import com.d_to_a.backend.entity.Club;
import com.d_to_a.backend.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public ResponseEntity<Club> createClub(@RequestParam String name,
                                           @RequestParam String stack,
                                           @RequestParam String description) {
        return ResponseEntity.ok(clubService.createClub(name, stack, description));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Club>> getAll() {
        return ResponseEntity.ok(clubService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clubService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
