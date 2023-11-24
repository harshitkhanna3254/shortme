package com.comp519.shortme.dto;

import com.comp519.shortme.validators.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * DTO for URL shortening request.
 */
@Getter
public class UrlShortenRequest {
    final String NOT_BLANK_MESSAGE = "The `longUrl` field cannot be blank";
    final String INVALID_URL_MESSAGE = "The provided URL is invalid." +
            "It must start with `http://` or `https://` and it may or may not contain `www.`";

    @NotBlank(message = NOT_BLANK_MESSAGE)
    @ValidUrl(message = INVALID_URL_MESSAGE)
    private String longUrl;
}

