package org.mygoal.fitnessapp.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.dto.AthleteDto;
import org.mygoal.fitnessapp.backend.dto.BodyMeasurementsDto;
import org.mygoal.fitnessapp.backend.dto.PersonalDataDto;
import org.mygoal.fitnessapp.backend.service.AthleteService;
import org.mygoal.fitnessapp.backend.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/athletes")
@RequiredArgsConstructor
@Tag(name = "Athletes", description = "Operations related to athletes")
public class AthleteController {

    private final AthleteService athleteService;
    private final EmailService emailService;

    @Operation(summary = "Get all athletes", description = "Returns a list of all registered athletes")
    @GetMapping
    public ResponseEntity<List<AthleteDto>> getAllAthletes() {
        List<AthleteDto> athletes = athleteService.getAllAthletes();
        return ResponseEntity.ok(athletes);
    }

    @Operation(summary = "Get athlete by ID", description = "Returns an athlete by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<AthleteDto> getAthleteById(
            @Parameter(description = "The ID of the athlete") @PathVariable Long id) {
        AthleteDto athlete = athleteService.getAthleteDtoById(id);
        return ResponseEntity.ok(athlete);
    }

    @Operation(summary = "Delete athlete by ID", description = "Deletes an athlete and their associated data by ID")
    @DeleteMapping("/{id}") // Fixed endpoint path
    public ResponseEntity<Void> deleteAthleteById(
            @Parameter(description = "The ID of the athlete to delete") @PathVariable Long id) {
        athleteService.deleteAthleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search athlete by email", description = "Finds an athlete using their email address")
    @GetMapping("/search")
    public ResponseEntity<AthleteDto> searchAthleteByEmail(
            @Parameter(description = "The email of the athlete") @RequestParam String email) {
        AthleteDto athlete = athleteService.findByEmail(email);
        return ResponseEntity.ok(athlete);
    }

    @Operation(summary = "Update athlete personal data",
            description = "Updates personal details such as name, date of birth, or contact information")
    @PatchMapping("/{id}/personal")
    public ResponseEntity<AthleteDto> updateAthletePersonal(
            @PathVariable Long id,
            @RequestBody PersonalDataDto personalData) {
        AthleteDto updatedAthlete = athleteService.updateAthletePersonal(id, personalData);
        return ResponseEntity.ok(updatedAthlete);
    }

    @Operation(summary = "Update athlete body parameters",
            description = "Updates body measurements such as weight, height, and BMI")
    @PatchMapping("/{id}/params")
    public ResponseEntity<AthleteDto> updateAthleteParams(
            @PathVariable Long id,
            @RequestBody BodyMeasurementsDto parameters) {
        AthleteDto updatedAthlete = athleteService.updateAthleteBMParams(id, parameters);
        return ResponseEntity.ok(updatedAthlete);
    }

    /**
     * Sends an email to the athlete containing their latest physical measurements.
     *
     * @param id The ID of the athlete.
     * @return HTTP 200 OK if the email is sent successfully, otherwise HTTP 500 if an error occurs.
     */
    @Operation(summary = "Send athlete's body measurements via email",
            description = "Sends an email containing the athlete's latest body measurements")
    @PostMapping("/{id}/send-measurements")
    public ResponseEntity<String> sendEmailParams(@PathVariable Long id) {
        try {
            emailService.sendEmailParams(athleteService.getAthleteById(id));
            return ResponseEntity.ok("Email sent successfully");
        } catch (MailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }
}
