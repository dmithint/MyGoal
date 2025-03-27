package org.mygoal.fitnessapp.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.mygoal.fitnessapp.backend.model.TrainingStatus;

/**
 * Data Transfer Object (DTO) for updating the status of a training session in the FitnessApp.
 * <p>
 * This class encapsulates the new status to be applied to a training session, ensuring it is not null.
 */
@Getter
@Setter
@Schema(description = "DTO for updating the status of a training session")
public class TrainingStatusUpdateRequest {

    /**
     * The new status to be applied to the training session (e.g., SCHEDULED, IN_PROGRESS, COMPLETED).
     */
    @NotNull(message = "Status cannot be null")
    @Schema(description = "New status of the training session (e.g., SCHEDULED, IN_PROGRESS, COMPLETED)",
            example = "IN_PROGRESS", required = true)
    private TrainingStatus status;
}