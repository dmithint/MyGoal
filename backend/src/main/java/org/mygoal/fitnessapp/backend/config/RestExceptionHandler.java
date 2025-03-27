package org.mygoal.fitnessapp.backend.config;

import org.mygoal.fitnessapp.backend.dto.ErrorDto;
import org.mygoal.fitnessapp.backend.exceptions.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Handles exceptions thrown by the application and converts them to HTTP responses with appropriate status codes and error messages.
 */
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {AppException.class})
    public ResponseEntity<ErrorDto> handleAppException(AppException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorDto.builder().message(ex.getMessage()).build());
    }

}