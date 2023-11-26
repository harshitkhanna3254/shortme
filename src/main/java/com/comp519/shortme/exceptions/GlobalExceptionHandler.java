package com.comp519.shortme.exceptions;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.comp519.shortme.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.MalformedURLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ExceptionResponseDto errorResponse = new ExceptionResponseDto(
                "Validation Error",
                errorMessage,
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<ExceptionResponseDto> handleMalformedURLException(MalformedURLException ex) {
        ExceptionResponseDto errorResponse = new ExceptionResponseDto(
                "Invalid URL",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleUserNotFoundException(UrlNotFoundException ex) {
        ExceptionResponseDto errorResponse = new ExceptionResponseDto(
                "URL not found in your created URLs.",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UrlNotCreatedByUser.class)
    public ResponseEntity<ExceptionResponseDto> handleUserNotFoundException(UrlNotCreatedByUser ex) {
        ExceptionResponseDto errorResponse = new ExceptionResponseDto(
                "The requested URL was not created by you.",
                ex.getMessage(),
                HttpStatus.FORBIDDEN.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
        ExceptionResponseDto errorResponse = new ExceptionResponseDto(
                "User not found in the database",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponseDto> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ExceptionResponseDto errorResponse = new ExceptionResponseDto(
                "Invalid Credentials",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDto> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ExceptionResponseDto errorResponse = new ExceptionResponseDto(
                "User already present",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class, JWTDecodeException.class})
    public ResponseEntity<ExceptionResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ExceptionResponseDto errorResponse = new ExceptionResponseDto(
                "Illegal Arguments",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseDto> handleRuntimeException(RuntimeException ex) {
        ExceptionResponseDto errorResponse = new ExceptionResponseDto(
                "Internal Server Error",
                "An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleException(Exception ex) {
        ExceptionResponseDto errorResponse = new ExceptionResponseDto(
                "Internal Server Error",
                "An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
