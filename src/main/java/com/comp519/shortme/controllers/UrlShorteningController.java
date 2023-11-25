package com.comp519.shortme.controllers;

import com.comp519.shortme.dto.UrlShortenRequestDto;
import com.comp519.shortme.dto.UrlShortenResponseDto;
import com.comp519.shortme.services.UrlShorteningService;
import com.comp519.shortme.utils.Utils;
import com.comp519.shortme.validators.ValidUrl;
import com.google.api.gax.rpc.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api")
@Validated
public class UrlShorteningController {

    private final UrlShorteningService urlShorteningService;

    private final Utils utils;

    @Autowired
    public UrlShorteningController(Utils utils, UrlShorteningService urlShorteningService) {
        this.utils = utils;
        this.urlShorteningService = urlShorteningService;
    }

    // API to shorten a URL
    @PostMapping("/shorten")
    public ResponseEntity<UrlShortenResponseDto> shortenUrl(@RequestBody @Valid UrlShortenRequestDto request) {
        String longUrl = request.getLongUrl();

        // Generate the short link
        String shortLink = utils.generateShortLink();

        // Save to Big Table
        String timestamp = urlShorteningService.saveUrlMapping(shortLink, longUrl);

        // Convert Short Link to URL
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String shortUrl = baseUrl + "/" + shortLink;

        System.out.println("Short Url: " + shortUrl);

        // Response
        UrlShortenResponseDto response = new UrlShortenResponseDto(shortUrl, longUrl, timestamp);

        return ResponseEntity.ok(response);
    }

    // API to retrieve the original URL
    @GetMapping("/retrieve")
    public ResponseEntity<String> retrieveUrl(@RequestParam @NotBlank @ValidUrl String shortUrl)
            throws MalformedURLException, NotFoundException {

        String shortLink = utils.retrieveShortLinkFromUrl(shortUrl);

        String longUrl = urlShorteningService.retrieveOriginalUrl(shortLink);

        if (longUrl == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Short URL not found: " + shortUrl);

        // If the long URL is found, return it in the response.
        return ResponseEntity.ok(longUrl);
    }
}
