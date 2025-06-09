package com.d_to_a.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordChangeRequest {
    private String oldPassword;
    private String newPassword;
}
