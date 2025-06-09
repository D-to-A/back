package com.d_to_a.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AwardRequest {

    private String title;
    private String description;

    @NotNull(message = "수상 날짜는 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate awardedAt;
}
