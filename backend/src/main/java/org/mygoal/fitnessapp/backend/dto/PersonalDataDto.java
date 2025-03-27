package org.mygoal.fitnessapp.backend.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing personal data of a user in the FitnessApp.
 * <p>
 * This class encapsulates basic personal information including first name, last name, and email address.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO representing a user's personal data, including first name, last name, and email")
public class PersonalDataDto {

    /**
     * The first name of the user.
     */
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    /**
     * The last name of the user.
     */
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    /**
     * The email address of the user.
     */
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;
}