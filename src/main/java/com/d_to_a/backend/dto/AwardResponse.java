package com.d_to_a.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AwardResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate awardedAt;
}
