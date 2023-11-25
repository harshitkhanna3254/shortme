package com.comp519.shortme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for URL shortening response.
 */
@Getter
@Setter
@AllArgsConstructor
public class UrlShortenResponseDto {

    private String shortUrl;
    private String longUrl;
    private String createdAt;
}
