package com.comp519.shortme.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO for URL shortening response.
 */
@Getter
@Setter
public class UrlShortenResponse {

    private String shortUrl;
    private String longUrl;
    private String createdAt;
}
