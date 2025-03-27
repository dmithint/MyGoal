package org.mygoal.fitnessapp.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing user credentials in the FitnessApp.
 * <p>
 * This class encapsulates the email and password required for user authentication.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO representing user credentials for authentication, including email and password")
public class CredentialsDto {

    /**
     * The email address of the user.
     */
    @Schema(description = "Email address of the user", example = "user@example.com", required = true)
    private String email;

    /**
     * The password of the user.
     */
    @Schema(description = "Password of the user", example = "securePassword123", required = true)
    private String password;
}