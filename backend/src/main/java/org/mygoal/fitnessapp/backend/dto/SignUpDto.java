package org.mygoal.fitnessapp.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mygoal.fitnessapp.backend.model.RoleType;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for user sign-up in the FitnessApp.
 * <p>
 * This class encapsulates the data required to register a new user, including personal details,
 * email, password, and roles. All fields are validated to ensure they meet specific constraints.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO for user sign-up, including personal details, email, password, and roles")
public class SignUpDto {

    /**
     * The first name of the user.
     */
    @NotBlank(message = "First name cannot be empty")
    @Size(max = 50, message = "First name too long")
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;

    /**
     * The last name of the user.
     */
    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 50, message = "Last name too long")
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;

    /**
     * The email address of the user.
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Schema(description = "Email address of the user", example = "john.doe@example.com", required = true)
    private String email;

    /**
     * The password of the user.
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 50, message = "Password must be 8-50 characters")
    @Schema(description = "Password of the user (8-50 characters)", example = "securePass123", required = true)
    private String password;

    /**
     * The set of roles assigned to the user.
     */
    @NotEmpty(message = "At least one role is required")
    @Schema(description = "Set of roles assigned to the user (e.g., ATHLETE, COACH)",
            example = "[\"ATHLETE\", \"COACH\"]", required = true)
    private Set<RoleType> roles;
}