package org.mygoal.fitnessapp.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing error messages in the FitnessApp.
 * <p>
 * This class encapsulates a single error message to be returned in API responses
 * when an error occurs.
 */
@AllArgsConstructor
@Data
@Builder
@Schema(description = "DTO representing an error message returned by the API")
public class ErrorDto {

    /**
     * The error message describing what went wrong.
     */
    @Schema(description = "Error message describing the issue", example = "Resource not found")
    private String message;
}