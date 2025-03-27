package org.mygoal.fitnessapp.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Data Transfer Object (DTO) representing an athlete's profile in the FitnessApp.
 * <p>
 * This class encapsulates basic athlete information such as ID, email, first name, last name,
 * and their body measurements.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO representing an athlete's profile, including personal details and body measurements")
public class AthleteDto {

    /**
     * The unique identifier of the athlete.
     */
    @Schema(description = "Unique identifier of the athlete", example = "1")
    private Long id;

    /**
     * The email address of the athlete.
     */
    @Schema(description = "Email address of the athlete", example = "john.doe@example.com")
    private String email;

    /**
     * The first name of the athlete.
     */
    @Schema(description = "First name of the athlete", example = "John")
    private String firstName;

    /**
     * The last name of the athlete.
     */
    @Schema(description = "Last name of the athlete", example = "Doe")
    private String lastName;

    /**
     * The body measurements associated with the athlete.
     */
    @Schema(description = "Body measurements of the athlete (e.g., height, weight)")
    private BodyMeasurementsDto bodyMeasurements;
}