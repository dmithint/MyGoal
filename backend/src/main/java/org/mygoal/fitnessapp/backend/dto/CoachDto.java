package org.mygoal.fitnessapp.backend.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a coach's profile in the FitnessApp.
 * <p>
 * This class encapsulates information about a coach, including their personal details,
 * average rating, specialization, and years of experience.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO representing a coach's profile, including personal details, rating, specialization, and experience")
public class CoachDto {

    /**
     * The unique identifier of the coach.
     */
    @Schema(description = "Unique identifier of the coach", example = "1")
    private Long id;

    /**
     * The first name of the coach.
     */
    @Schema(description = "First name of the coach", example = "Jane")
    private String firstName;

    /**
     * The last name of the coach.
     */
    @Schema(description = "Last name of the coach", example = "Smith")
    private String lastName;

    /**
     * The email address of the coach.
     */
    @Schema(description = "Email address of the coach", example = "jane.smith@example.com")
    private String email;

    /**
     * The average rating of the coach based on feedback (e.g., out of 5).
     */
    @Schema(description = "Average rating of the coach (e.g., out of 5)", example = "4.5")
    private double averageRating;

    /**
     * The coach's area of specialization (e.g., strength training, yoga).
     */
    @Schema(description = "Area of specialization of the coach", example = "Strength Training")
    private String specialization;

    /**
     * The number of years of coaching experience.
     */
    @Schema(description = "Years of coaching experience", example = "10")
    private Integer experience;
}