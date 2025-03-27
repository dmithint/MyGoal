package org.mygoal.fitnessapp.backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a training session in the FitnessApp.
 * <p>
 * This class encapsulates details about a training session, including its name, description,
 * status, schedule, participants (coach and athletes), and rating.
 */
@Getter
@Setter
@Schema(description = "DTO representing a training session with details about its schedule, participants, and rating")
public class TrainingDto {

    /**
     * The unique identifier of the training session.
     */
    @Schema(description = "Unique identifier of the training session", example = "1")
    private Long id;

    /**
     * The name of the training session.
     */
    @Schema(description = "Name of the training session", example = "Morning Strength Training")
    private String name;

    /**
     * A brief description of the training session.
     */
    @Schema(description = "Description of the training session", example = "A high-intensity strength workout")
    private String description;

    /**
     * The current status of the training session (e.g., Scheduled, In Progress, Completed).
     */
    @Schema(description = "Status of the training session", example = "Scheduled")
    private String status;

    /**
     * The maximum number of athletes allowed in the training session.
     */
    @Schema(description = "Maximum number of athletes allowed", example = "10")
    private Integer maxAthletes;

    /**
     * The start date and time of the training session.
     */
    @Schema(description = "Start date and time of the training session", example = "2025-03-28T09:00:00")
    private LocalDateTime start;

    /**
     * The end date and time of the training session.
     */
    @Schema(description = "End date and time of the training session", example = "2025-03-28T10:00:00")
    private LocalDateTime end;

    /**
     * The coach assigned to the training session.
     */
    @Schema(description = "Coach assigned to the training session")
    private CoachDto coach;

    /**
     * The list of athletes enrolled in the training session.
     */
    @Schema(description = "List of athletes enrolled in the training session")
    private List<AthleteDto> athletes;

    /**
     * The average rating of the training session based on feedback.
     */
    @Schema(description = "Average rating of the training session (e.g., out of 5)", example = "4.8")
    private Double averageRating;

    /**
     * Nested DTO representing a coach for the training session.
     */
    @Getter
    @Setter
    @Schema(description = "Nested DTO representing a coach for the training session")
    public static class CoachDto {
        /**
         * The unique identifier of the coach.
         */
        @Schema(description = "Unique identifier of the coach", example = "2")
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
         * The coach's area of specialization.
         */
        @Schema(description = "Area of specialization of the coach", example = "Strength Training")
        private String specialization;

        /**
         * The number of years of coaching experience.
         */
        @Schema(description = "Years of coaching experience", example = "8")
        private Integer experience;

        /**
         * The average rating of the coach based on feedback.
         */
        @Schema(description = "Average rating of the coach (e.g., out of 5)", example = "4.7")
        private Double averageRating;
    }

    /**
     * Nested DTO representing an athlete enrolled in the training session.
     */
    @Getter
    @Setter
    @Schema(description = "Nested DTO representing an athlete enrolled in the training session")
    public static class AthleteDto {
        /**
         * The unique identifier of the athlete.
         */
        @Schema(description = "Unique identifier of the athlete", example = "3")
        private Long id;

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
    }
}