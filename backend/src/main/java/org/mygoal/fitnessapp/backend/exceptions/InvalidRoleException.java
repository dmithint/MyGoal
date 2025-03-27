package org.mygoal.fitnessapp.backend.exceptions;

import org.springframework.http.HttpStatus;


public class InvalidRoleException extends AppException {
    public InvalidRoleException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
