package com.modexa.carelink.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank
    private String token;
    @NotBlank
    private String newPassword;
}
