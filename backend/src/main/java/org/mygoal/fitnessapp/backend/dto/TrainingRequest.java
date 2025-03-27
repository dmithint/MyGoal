package org.mygoal.fitnessapp.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mygoal.fitnessapp.backend.model.TrainingStatus;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for creating or updating a training session request in the FitnessApp.
 * <p>
 * This class encapsulates the details required to schedule a training session, including the coach,
 * name, description, status, maximum number of athletes, and start/end times.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO for creating or updating a training session request, including coach, schedule, and details")
public class TrainingRequest {

    /**
     * The unique identifier of the coach assigned to the training session.
     */
    @Schema(description = "Unique identifier of the coach assigned to the training", example = "2")
    private Long coachId;

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
     * The status of the training session (e.g., SCHEDULED, IN_PROGRESS, COMPLETED).
     */
    @Schema(description = "Status of the training session", example = "SCHEDULED")
    private TrainingStatus status;

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
}