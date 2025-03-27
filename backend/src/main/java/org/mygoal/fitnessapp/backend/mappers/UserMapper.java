package org.mygoal.fitnessapp.backend.mappers;

import org.mygoal.fitnessapp.backend.dto.UserDto;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.mygoal.fitnessapp.backend.model.Role;
import org.mygoal.fitnessapp.backend.model.User;

/**
 * Mapper class for converting {@link User} entities to {@link UserDto} objects in the FitnessApp.
 * <p>
 * This component provides methods to transform domain model objects into data transfer objects,
 * facilitating the separation of concerns between the persistence layer and API responses.
 */
@Component
public class UserMapper {

    /**
     * Converts a {@link User} entity to a {@link UserDto}.
     * <p>
     * This method maps the user's properties (ID, first name, last name, email, and roles) to a DTO.
     * If the input user is null, the method returns null.
     *
     * @param user The {@link User} entity to convert, or null.
     * @return A {@link UserDto} object with the mapped properties, or null if the input is null.
     */
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(mapRoles(user.getRoles()))
                .build();
    }

    /**
     * Maps a set of {@link Role} entities to a set of role names as strings.
     * <p>
     * This helper method extracts the name of each role (assumed to be an enum or string representation)
     * and collects them into a {@link Set} of strings for use in the {@link UserDto}.
     *
     * @param roles The set of {@link Role} entities to map.
     * @return A {@link Set} of role names as strings.
     */
    private Set<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}