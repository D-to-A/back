package com.d_to_a.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenReissueRequest {
    private String refreshToken;
}
