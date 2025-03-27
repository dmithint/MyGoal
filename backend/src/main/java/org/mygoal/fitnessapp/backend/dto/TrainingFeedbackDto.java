package org.mygoal.fitnessapp.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing feedback for a training session in the FitnessApp.
 * <p>
 * This class encapsulates feedback details, including the rating, comment, and references to
 * the training session, athlete, and coach involved.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing feedback for a training session, including rating, comment, and participant details")
public class TrainingFeedbackDto {

    /**
     * The unique identifier of the feedback.
     */
    @Schema(description = "Unique identifier of the feedback", example = "1")
    private Long id;

    /**
     * The identifier of the training session this feedback pertains to.
     */
    @Schema(description = "Identifier of the associated training session", example = "10")
    private Long trainingId;

    /**
     * The athlete who provided the feedback.
     */
    @Schema(description = "Athlete who provided the feedback")
    private Athlete athlete;

    /**
     * The rating given to the training session (e.g., out of 5).
     */
    @Schema(description = "Rating given to the training session (e.g., 1-5)", example = "4")
    private Integer rating;

    /**
     * The comment provided by the athlete about the training session.
     */
    @Schema(description = "Comment about the training session", example = "Great session, very challenging!")
    private String comment;

    /**
     * The name of the training session.
     */
    @Schema(description = "Name of the training session", example = "Morning Strength Training")
    private String trainingName;

    /**
     * The coach who led the training session.
     */
    @Schema(description = "Coach who led the training session")
    private Coach coach;

    /**
     * Nested DTO representing the athlete who provided the feedback.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Nested DTO representing the athlete who provided the feedback")
    public static class Athlete {
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

    /**
     * Nested DTO representing the coach who led the training session.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Nested DTO representing the coach who led the training session")
    public static class Coach {
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
    }
}