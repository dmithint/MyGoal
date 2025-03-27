package org.mygoal.fitnessapp.backend.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.dto.CoachDto;
import org.mygoal.fitnessapp.backend.dto.PersonalDataDto;
import org.mygoal.fitnessapp.backend.service.CoachService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * This class defines the REST controller for managing coach data.
 * It handles requests to get, update, and delete coach information.
 */
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/coaches")
@Tag(name = "Coach", description = "Endpoints for managing coach data.")
public class CoachController {

    private final CoachService coachService;

    /**
     * This method fetches a list of all registered coaches.
     *
     * @return a list of all registered coaches in the system
     */
    @Operation(summary = "Get all coaches", description = "Fetch a list of all registered coaches in the system.")
    @GetMapping
    public ResponseEntity<List<CoachDto>> getCoaches() {
        return ResponseEntity.ok(coachService.getCoachesList());
    }

    /**
     * This method fetches the details of a coach by their ID.
     *
     * @param id the ID of the coach
     * @return the details of the coach with the given ID
     */
    @Operation(summary = "Get coach by ID", description = "Fetch coach details by ID.")
    @GetMapping("/{id}")
    public ResponseEntity<CoachDto> getCoachById(@PathVariable Long id) {
        return ResponseEntity.ok(coachService.getCoachById(id));
    }

    /**
     * This method deletes a coach and their associated data by ID.
     *
     * @param id the ID of the coach to delete
     * @return a response indicating success or failure
     */
    @Operation(summary = "Delete coach by ID", description = "Deletes a coach and their associated data by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoachById(
            @Parameter(description = "The ID of the coach to delete") @PathVariable Long id) {
        CoachDto coach = coachService.getCoachById(id);
        if (coach == null) {
            return ResponseEntity.notFound().build();
        }
        coachService.deleteCoachById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * This method updates the personal data of a coach.
     *
     * @param id the ID of the coach to update
     * @param personalData the personal data to update
     * @return the updated coach data
     */
    @Operation(summary = "Update coach personal data", description = "Update the personal data of a coach.")
    @PatchMapping("/{id}/personal")
    public ResponseEntity<CoachDto> updateCoachPersonal(
            @PathVariable Long id,
            @RequestBody PersonalDataDto personalData) {
        CoachDto updatedCoach = coachService.updateCoachPersonal(id, personalData);
        return ResponseEntity.ok(updatedCoach);
    }
}
