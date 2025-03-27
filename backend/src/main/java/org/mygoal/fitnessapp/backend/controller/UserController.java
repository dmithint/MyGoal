package org.mygoal.fitnessapp.backend.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.dto.UserDto;
import org.mygoal.fitnessapp.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * REST API controller for managing User data in the FitnessApp backend.
 * <p>
 * This controller provides endpoints to manage user profiles, including fetching
 * user details by ID and updating a user's email address.
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/users")
@Tag(name = "User Profile", description = "Endpoints for managing user profiles in the FitnessApp")
public class UserController {

    /**
     * The UserService instance used to interact with the database for user-related operations.
     */
    private final UserService userService;

    /**
     * GET endpoint to fetch a user's profile data by their ID.
     *
     * @param id The ID of the user to fetch.
     * @return A ResponseEntity object containing a UserDto object and an HTTP 200 (OK) status code.
     */
    @Operation(summary = "Get user by ID", description = "Retrieves a user's profile data based on their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID provided")
    })
    @GetMapping("")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "The ID of the user to fetch", required = true)
            @RequestParam Long id) {
        UserDto user = userService.getUserDtoById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * POST endpoint to change a user's email address.
     *
     * @param id    The ID of the user to change the email address for.
     * @param email The new email address.
     * @return A ResponseEntity object containing the updated UserDto object and an HTTP 200 (OK) status code.
     */
    @Operation(summary = "Change user email", description = "Updates the email address of a user identified by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID or email format")
    })
    @PostMapping("/email")
    public ResponseEntity<UserDto> changeUserEmail(
            @Parameter(description = "The ID of the user whose email will be updated", required = true)
            @RequestParam Long id,
            @Parameter(description = "The new email address to set", required = true)
            @RequestParam String email) {
        UserDto user = userService.changeUserEmail(id, email);
        return ResponseEntity.ok(user);
    }
}