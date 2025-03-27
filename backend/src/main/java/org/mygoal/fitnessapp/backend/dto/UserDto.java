package org.mygoal.fitnessapp.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Data Transfer Object (DTO) representing a user in the FitnessApp.
 * <p>
 * This class encapsulates user details including personal information, email, roles,
 * and an authentication token (if applicable).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO representing a user, including personal details, roles, and authentication token")
public class UserDto {

    /**
     * The unique identifier of the user.
     */
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    /**
     * The first name of the user.
     */
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    /**
     * The last name of the user.
     */
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    /**
     * The email address of the user.
     */
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    /**
     * The set of roles assigned to the user (e.g., ATHLETE, COACH, ADMIN).
     */
    @Schema(description = "Set of roles assigned to the user", example = "[\"ATHLETE\", \"COACH\"]")
    private Set<String> roles;

    /**
     * The authentication token for the user (optional, included after login).
     */
    @Schema(description = "Authentication token for the user (optional)", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
}