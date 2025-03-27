package org.mygoal.fitnessapp.backend.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base class for all application-specific exceptions in the FitnessApp.
 * <p>
 * This class extends {@link RuntimeException} and includes an HTTP status code to indicate
 * the type of error encountered. It serves as a foundation for custom exceptions throughout
 * the application, allowing consistent error handling and response generation.
 */
@Getter
public class AppException extends RuntimeException {

    /**
     * The HTTP status code associated with this exception (e.g., 400 for Bad Request, 404 for Not Found).
     */
    private final HttpStatus status;

    /**
     * Constructs an AppException with a message and HTTP status.
     *
     * @param message The error message describing the exception.
     * @param status  The HTTP status code to be returned in the API response.
     */
    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}