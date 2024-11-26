package com.modexa.carelink.dto.auth;

import com.modexa.carelink.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for user registration")
public class RegisterRequest {
    @Schema(description = "Username for the account", example = "johndoe")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Email address", example = "john.doe@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Password for the account", example = "securepassword123")
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(description = "User's first name", example = "John")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(description = "Contact phone number", example = "1234567890")
    private String contactNumber;

    @Schema(description = "Organization UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = false)
    private UUID organizationId;

    @Schema(description = "User role", example = "PHYSICIAN", allowableValues = {"ADMIN", "PHYSICIAN", "NURSE", "PHYSICIAN_ASSISTANT"})
    @NotNull(message = "Role is required")
    private Role role;

    @Schema(description = "Professional title", example = "Physician")
    private String professionalTitle;

    @Schema(description = "License or registration number", example = "12345")
    private String licenseNumber;
}
