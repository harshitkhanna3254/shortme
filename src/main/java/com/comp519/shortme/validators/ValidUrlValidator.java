package com.comp519.shortme.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link ValidUrl} annotation.
 */
public class ValidUrlValidator implements ConstraintValidator<ValidUrl, String> {

    @Override
    public void initialize(ValidUrl constraintAnnotation) {
        // Initialization logic can be added here if needed.
    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        // Won't ever reach here but still...
        if (url == null)
            return false;

        // Regex for URL validation.
        final String URL_REGEX = "http(s)?://www.*";
        return url.matches(URL_REGEX);
    }
}
