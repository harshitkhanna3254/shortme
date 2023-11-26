package com.comp519.shortme.dto;

import com.comp519.shortme.validators.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.comp519.shortme.constants.ApplicationConstants.INVALID_URL_MESSAGE;
import static com.comp519.shortme.constants.ApplicationConstants.NOT_BLANK_MESSAGE;

/**
 * DTO for URL shortening request.
 */
@Data
public class UrlRequestDto {


    @NotBlank(message = NOT_BLANK_MESSAGE)
    @ValidUrl(message = INVALID_URL_MESSAGE)
    private String url;
}

