package org.mygoal.fitnessapp.backend.controller;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.config.UserAuthProvider;
import org.mygoal.fitnessapp.backend.dto.*;
import org.mygoal.fitnessapp.backend.service.AthleteService;
import org.mygoal.fitnessapp.backend.service.CoachService;
import org.mygoal.fitnessapp.backend.service.TrainingService;
import org.mygoal.fitnessapp.backend.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * This class defines the REST controller for authentication.
 * It handles login and registration requests.
 * The login endpoint takes a CredentialsDto object as input and returns a UserDto object with a token.
 * The registration endpoint takes a SignUpDto object as input and returns a UserDto object with a token.
 */
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration.")
public class AuthController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    private final CoachService coachService;
    private final AthleteService athleteService;
    private final TrainingService trainingService;


    private static final String UTF8_BOM = "\uFEFF";
    private static final String CONTENT_DISPOSITION = "attachment; filename=";

    /**
     * Endpoint to generate a CSV report of all coaches.
     * The report includes the following fields: ID, First Name, Last Name, Email, Rating, Specialization, Experience.
     *
     * @return CSV report of all coaches
     * @throws IOException if there's an error while generating the CSV file
     */
    @Operation(summary = "Generate Coaches CSV Report", description = "Generates a CSV report containing all coaches and their details.")
    @GetMapping("/report/coaches")
    public ResponseEntity<Resource> generateCoachesCsvReport() throws IOException {
        List<CoachDto> coaches = coachService.getCoachesList();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "First Name", "Last Name", "Email", "Rating", "Specialization", "Experience"))) {

            writer.write(UTF8_BOM);
            for (CoachDto coach : coaches) {
                printer.printRecord(
                        coach.getId(),
                        nullSafeString(coach.getFirstName()),
                        nullSafeString(coach.getLastName()),
                        nullSafeString(coach.getEmail()),
                        coach.getAverageRating(),
                        nullSafeString(coach.getSpecialization()),
                        coach.getExperience()
                );
            }
            return createCsvResponse(stream, "coaches.csv");
        }
    }

    /**
     * Endpoint to generate a CSV report of all athletes.
     * The report includes the following fields: ID, First Name, Last Name, Email.
     *
     * @return CSV report of all athletes
     * @throws IOException if there's an error while generating the CSV file
     */
    @Operation(summary = "Generate Athletes CSV Report", description = "Generates a CSV report containing all athletes and their details.")
    @GetMapping("/report/athletes")
    public ResponseEntity<Resource> generateAthletesCsvReport() throws IOException {
        List<AthleteDto> athletes = athleteService.getAllAthletes();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "First Name", "Last Name", "Email"))) {

            writer.write(UTF8_BOM);
            for (AthleteDto athlete : athletes) {
                printer.printRecord(
                        athlete.getId(),
                        nullSafeString(athlete.getFirstName()),
                        nullSafeString(athlete.getLastName()),
                        nullSafeString(athlete.getEmail())
                );
            }
            return createCsvResponse(stream, "athletes.csv");
        }
    }

    /**
     * Endpoint to generate a CSV report of all trainings.
     * The report includes the following fields: ID, Name, Description, Status, Start, End, Coach, Athletes Count, Average Rating.
     *
     * @return CSV report of all trainings
     * @throws IOException if there's an error while generating the CSV file
     */
    @Operation(summary = "Generate Trainings CSV Report", description = "Generates a CSV report containing all trainings and their details.")
    @GetMapping("/report/trainings")
    public ResponseEntity<Resource> generateTrainingsCsvReport() throws IOException {
        List<TrainingDto> trainings = trainingService.getTrainingsWithFilter(null, null, null);
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "Name", "Description", "Status", "Start", "End", "Coach", "Athletes Count", "Average Rating"))) {

            writer.write(UTF8_BOM);
            for (TrainingDto training : trainings) {
                String coachName = training.getCoach() != null
                        ? nullSafeString(training.getCoach().getFirstName()) + " " +
                        nullSafeString(training.getCoach().getLastName())
                        : "N/A";

                printer.printRecord(
                        training.getId(),
                        nullSafeString(training.getName()),
                        nullSafeString(training.getDescription()),
                        training.getStatus(),
                        training.getStart(),
                        training.getEnd(),
                        coachName,
                        training.getAthletes() != null ? training.getAthletes().size() : 0,
                        training.getAverageRating()
                );
            }
            return createCsvResponse(stream, "trainings.csv");
        }
    }

    /**
     * Utility method to create a response containing the generated CSV file.
     *
     * @param stream the ByteArrayOutputStream containing the CSV content
     * @param filename the name of the CSV file
     * @return ResponseEntity containing the CSV file
     * @throws IOException if an error occurs during the file response creation
     */
    private ResponseEntity<Resource> createCsvResponse(ByteArrayOutputStream stream, String filename) throws IOException {
        stream.flush();
        ByteArrayResource resource = new ByteArrayResource(stream.toByteArray());
        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, CONTENT_DISPOSITION + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    /**
     * Utility method to handle null values in string inputs.
     *
     * @param value the string value to check
     * @return the input value if it's not null, otherwise an empty string
     */
    private String nullSafeString(String value) {
        return value != null ? value : "";
    }

    /**
     * This method handles login requests. It takes a CredentialsDto object as input and returns a UserDto object with a token.
     *
     * @param credentialsDto the credentials of the user
     * @param response the HttpServletResponse to add the cookie
     * @return the user with a token
     */
    @Operation(summary = "User Login", description = "Login a user with credentials and return a UserDto object with a token.")
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto, HttpServletResponse response) {
        UserDto userDto = userService.login(credentialsDto);
        String token = userAuthProvider.createToken(userDto.getEmail());
        userDto.setToken(token);

        // Create cookie with token
        Cookie cookie = new Cookie("token", token);
//        cookie.setHttpOnly(true); // For security: mitigate XSS attacks
//        cookie.setSecure(true); // Ensure cookie is used over HTTPS
        cookie.setPath("/"); // Accessible throughout the domain
        cookie.setMaxAge(7 * 24 * 60 * 60); // Set cookie expiration (7 days)

        // Add the cookie to the response
        response.addCookie(cookie);

        return ResponseEntity.ok(userDto);
    }

    /**
     * This method handles registration requests. It takes a SignUpDto object as input and returns a UserDto object with a token.
     *
     * @param user the user to be registered
     * @param response the HttpServletResponse to add the cookie
     * @return the registered user with a token
     */
    @Operation(summary = "User Registration", description = "Register a new user and return a UserDto object with a token.")
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user, HttpServletResponse response) {
        UserDto createdUser = userService.register(user);
        String token = userAuthProvider.createToken(createdUser.getEmail());
        createdUser.setToken(token);

        // Create cookie with token
        Cookie cookie = new Cookie("token", token);
//        cookie.setHttpOnly(true); // For security: mitigate XSS attacks
//        cookie.setSecure(true); // Ensure cookie is used over HTTPS
        cookie.setPath("/"); // Accessible throughout the domain
        cookie.setMaxAge(7 * 24 * 60 * 60); // Set cookie expiration (7 days)

        response.addCookie(cookie);

        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }
}
