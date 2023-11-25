package com.comp519.shortme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * DTO for User Registration request.
 */
@Getter
public class UserRegisterRequestDto {

    final String NOT_BLANK_MESSAGE = "";
    final String INVALID_USERNAME_MESSAGE = "";

    @NotBlank(message = "Username is required")
    @Size(min = 4, message = "Username must be at least 4 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username can only contain alphanumeric characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Password can only contain alphanumeric characters")
    private String password;

    @NotBlank(message = "First Name is required")
    @Size(min = 4, message = "First Name must be at least 4 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "First Name can only contain alphanumeric characters")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(min = 4, message = "Last Name must be at least 4 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Last Name can only contain alphanumeric characters")
    private String lastName;
}
