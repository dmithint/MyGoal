package org.mygoal.fitnessapp.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.exceptions.AppException;
import org.mygoal.fitnessapp.backend.model.RoleType;
import org.mygoal.fitnessapp.backend.model.User;
import org.mygoal.fitnessapp.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@Tag(name = "Admins", description = "Operations related to administrators")
public class AdminController {

    private final UserRepository userRepository;

    /**
     * Retrieves an administrator by their ID.
     *
     * @param id the ID of the administrator
     * @return the administrator if found
     * @throws AppException if the administrator is not found or the user is not an admin
     */
    @Operation(summary = "Get admin by ID", description = "Returns an administrator by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getAdminById(
            @Parameter(description = "The ID of the administrator") @PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Admin not found", HttpStatus.NOT_FOUND));

        if (user.getRoles().stream().anyMatch(it -> it.getName() == RoleType.ADMIN)) {
            return ResponseEntity.ok(user);
        } else {
            throw new AppException("User is not an admin", HttpStatus.NOT_FOUND);
        }
    }
}
