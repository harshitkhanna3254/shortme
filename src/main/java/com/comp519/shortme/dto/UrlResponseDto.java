package com.comp519.shortme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for URL shortening response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlResponseDto {

    private String shortUrl;
    private String longUrl;
    private String createdAt;
    private String username;
    private String clickCount;
    private String timestamps;
}
