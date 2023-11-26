package com.comp519.shortme.constants;

public class ApplicationConstants {
    // Success Messages
    public static final String REGISTER_SUCCESSFUL_MESSAGE = "You've been successfully registered. Please log in now.";
    public static final String LOGIN_SUCCESSFUL_MESSAGE = "Login successful.";
    public static final String URL_SHORTENED_SUCCESSFULLY_MESSAGE = "URL shortened successfully.";
    public static final String URL_RETRIEVED_SUCCESSFULLY_MESSAGE = "URL retrieved successfully.";

    // Client Error Messages
    public static final String BAD_REQUEST_MESSAGE = "The request is invalid or malformed.";
    public static final String USERNAME_ALREADY_EXISTS_MESSAGE = "Username already exists. Please choose a different username.";
    public static final String INVALID_LOGIN_CREDENTIALS_MESSAGE = "Invalid login credentials. Please try again.";
    public static final String USER_ALREADY_EXISTS_MESSAGE = "A user with the given username already exists.";
    public static final String UNAUTHORIZED_MESSAGE = "You are not authorized to perform this action. Please log in.";
    public static final String FORBIDDEN_MESSAGE = "Access denied. You do not have permission to access this resource.";
    public static final String NOT_FOUND_MESSAGE = "The requested resource was not found.";
    public static final String METHOD_NOT_ALLOWED_MESSAGE = "The requested HTTP method is not allowed for this resource.";
    public static final String INVALID_JWT_MESSAGE = "The JWT token in invalid. Please Login to continue.";
    // Server Error Messages
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "An internal server error occurred. Please try again later.";
    public static final String SERVICE_UNAVAILABLE_MESSAGE = "The service is currently unavailable. Please try again later.";

    // Other Messages
    public static final String USER_LOGOUT_SUCCESSFUL_MESSAGE = "You have been successfully logged out.";
}
