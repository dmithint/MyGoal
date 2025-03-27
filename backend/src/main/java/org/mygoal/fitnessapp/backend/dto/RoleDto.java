package org.mygoal.fitnessapp.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a role in the FitnessApp.
 * <p>
 * This class encapsulates the identifier and name of a role, typically used to define
 * user permissions or categories (e.g., Athlete, Coach, Admin).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO representing a role within the FitnessApp, including its ID and name")
public class RoleDto {

    /**
     * The unique identifier of the role.
     */
    @Schema(description = "Unique identifier of the role", example = "1")
    private Long id;

    /**
     * The name of the role.
     */
    @Schema(description = "Name of the role (e.g., Athlete, Coach, Admin)", example = "Coach")
    private String name;
}