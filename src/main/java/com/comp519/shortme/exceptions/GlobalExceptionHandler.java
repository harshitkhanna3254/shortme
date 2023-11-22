package com.comp519.shortme.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Extract error details from the binding result
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        // You could inspect the error message or the field to determine if it's a URL error
        if(errorMessage.startsWith("Invalid URL")) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid URL provided");
            response.put("details", errorMessage);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Handle other validation errors generically
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    // Generic exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
