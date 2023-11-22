package com.comp519.shortme.dto;

import com.comp519.shortme.validators.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlShortenRequest {
    @NotBlank(message = "The URL cannot be blank")
    @ValidUrl(message = "Invalid URL format")
    private String longUrl;

}

