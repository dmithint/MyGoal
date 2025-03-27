package org.mygoal.fitnessapp.backend.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.dto.TrainingDto;
import org.mygoal.fitnessapp.backend.dto.TrainingFeedbackDto;
import org.mygoal.fitnessapp.backend.dto.TrainingRequest;
import org.mygoal.fitnessapp.backend.dto.TrainingStatusUpdateRequest;
import org.mygoal.fitnessapp.backend.model.Training;
import org.mygoal.fitnessapp.backend.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * REST API controller for managing Training data in the FitnessApp backend.
 * <p>
 * This controller provides endpoints to manage training sessions, including creating, fetching,
 * filtering, enrolling users, rating, and deleting training sessions.
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/trainings")
@Tag(name = "Training", description = "Endpoints for managing training sessions in the FitnessApp")
public class TrainingController {

    private final TrainingService trainingService;

    @Operation(summary = "Create a new training session", description = "Creates a new training session based on the provided request data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training session created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<Training> createTraining(
            @Parameter(description = "Details of the training session to create", required = true)
            @RequestBody @Valid TrainingRequest trainingRequest) {
        Training createdTraining = trainingService.createTraining(trainingRequest);
        return ResponseEntity.created(URI.create("/trainings/" + createdTraining.getId())).body(createdTraining);
    }

    @Operation(summary = "Get all training sessions", description = "Retrieves a list of all training sessions without filters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of training sessions retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<TrainingDto>> getAllTrainings() {
        return ResponseEntity.ok(trainingService.getTrainingsWithFilter(null, null, null));
    }

    @Operation(summary = "Get all training feedbacks", description = "Fetches a list of all feedback submitted for training sessions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of feedback retrieved successfully")
    })
    @GetMapping("/feedbacks")
    public ResponseEntity<List<TrainingFeedbackDto>> getAllFeedbacks() {
        return ResponseEntity.ok(trainingService.getAllFeedbacks());
    }

    @Operation(summary = "Delete a training feedback", description = "Removes a specific feedback by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Feedback deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Feedback not found")
    })
    @DeleteMapping("/feedbacks/{id}")
    public ResponseEntity<Void> deleteFeedbackById(
            @Parameter(description = "ID of the feedback to delete", required = true)
            @PathVariable Long id) {
        trainingService.deleteTrainingFeedback(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search training sessions with filters", description = "Fetches training sessions based on optional filters for athlete ID, coach ID, and date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered list of training sessions retrieved successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<List<TrainingDto>> searchTrainings(
            @Parameter(description = "Filter by athlete ID (optional)") @RequestParam(required = false) Long athleteId,
            @Parameter(description = "Filter by coach ID (optional)") @RequestParam(required = false) Long couchId,
            @Parameter(description = "Filter by training date (optional)") @RequestParam(required = false) LocalDateTime date) {
        return ResponseEntity.ok(trainingService.getTrainingsWithFilter(athleteId, couchId, date));
    }

    @Operation(summary = "Enroll an athlete in a training session", description = "Adds an athlete to a specified training session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Athlete enrolled successfully"),
            @ApiResponse(responseCode = "404", description = "Training or athlete not found")
    })
    @PatchMapping("/{trainingId}/join/{athleteId}")
    public ResponseEntity<String> joinAthleteInTraining(
            @Parameter(description = "ID of the athlete to enroll", required = true) @PathVariable Long athleteId,
            @Parameter(description = "ID of the training session", required = true) @PathVariable Long trainingId) {
        trainingService.enrollAthleteInTraining(athleteId, trainingId);
        return ResponseEntity.ok("User successfully enrolled in training.");
    }

    @Operation(summary = "Cancel an athlete's enrollment", description = "Removes an athlete from a specified training session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Training or athlete not found")
    })
    @PatchMapping("/{trainingId}/cancel/{athleteId}")
    public ResponseEntity<String> cancelAthleteInTraining(
            @Parameter(description = "ID of the athlete to remove", required = true) @PathVariable Long athleteId,
            @Parameter(description = "ID of the training session", required = true) @PathVariable Long trainingId) {
        trainingService.cancelAthleteInTraining(athleteId, trainingId);
        return ResponseEntity.ok("User successfully removed from training.");
    }

    @Operation(summary = "Update training session status", description = "Modifies the status of a specified training session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status provided"),
            @ApiResponse(responseCode = "404", description = "Training not found")
    })
    @PatchMapping("/{trainingId}/status")
    public ResponseEntity<String> updateTrainingStatus(
            @Parameter(description = "ID of the training session", required = true) @PathVariable Long trainingId,
            @Parameter(description = "Request containing the new status", required = true)
            @RequestBody @Valid TrainingStatusUpdateRequest request) {
        trainingService.updateTrainingStatus(trainingId, request.getStatus());
        return ResponseEntity.ok("Training status updated successfully.");
    }

    @Operation(summary = "Delete a training session", description = "Permanently deletes a specified training session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training session deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Training not found")
    })
    @DeleteMapping("/{trainingId}")
    public ResponseEntity<String> deleteTraining(
            @Parameter(description = "ID of the training session to delete", required = true)
            @PathVariable Long trainingId) {
        trainingService.deleteTraining(trainingId);
        return ResponseEntity.ok("Training session successfully deleted.");
    }

    @Operation(summary = "Rate a training session", description = "Allows an athlete to submit a rating and comment for a training session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating submitted successfully"),
            @ApiResponse(responseCode = "404", description = "Training or athlete not found"),
            @ApiResponse(responseCode = "400", description = "Invalid rating or comment")
    })
    @PatchMapping("/{trainingId}/rate/{athleteId}")
    public ResponseEntity<String> rateTraining(
            @Parameter(description = "ID of the training session", required = true) @PathVariable Long trainingId,
            @Parameter(description = "ID of the athlete submitting the rating", required = true) @PathVariable Long athleteId,
            @Parameter(description = "Rating value (e.g., 1-5)", required = true) @RequestParam int rating,
            @Parameter(description = "Comment about the training session", required = true) @RequestParam String comment) {
        trainingService.rateTraining(athleteId, trainingId, rating, comment);
        return ResponseEntity.ok("Rating submitted successfully");
    }
}