package com.modexa.carelink.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
