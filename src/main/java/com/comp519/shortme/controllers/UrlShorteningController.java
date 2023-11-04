package com.comp519.shortme.controllers;

import com.comp519.shortme.validators.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class UrlShorteningController {

    // API to shorten a URL
    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody @NotBlank @ValidUrl String longUrl) {
        // Logic for shortening the URL will be implemented here
        return ResponseEntity.ok("Short URL");
    }

    // API to retrieve the original URL
    @GetMapping("/retrieve")
    public ResponseEntity<String> retrieveUrl(@RequestParam @NotBlank @ValidUrl String shortUrl) {
        // Logic for retrieving the original URL will be implemented here
        return ResponseEntity.ok("Long URL");
    }
}
