package com.comp519.shortme.controllers;

import com.comp519.shortme.dto.UrlResponseDto;
import com.comp519.shortme.services.AnalyticsService;
import com.comp519.shortme.services.UrlShorteningService;
import com.google.api.gax.rpc.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

@RestController
@Validated
public class UrlShortenRedirectController {

    private final UrlShorteningService urlShorteningService;
    private final AnalyticsService analyticsService;

    public UrlShortenRedirectController(UrlShorteningService urlShorteningService, AnalyticsService analyticsService) {
        this.urlShorteningService = urlShorteningService;
        this.analyticsService = analyticsService;
    }

    // API to redirect user to the long url
    @GetMapping("/{shortLink}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortLink) throws NotFoundException, ExecutionException, InterruptedException, MalformedURLException {
        UrlResponseDto urlResponseDto = urlShorteningService.retrieveOriginalUrlWithoutAuth(shortLink);

        String originalUrl = urlResponseDto.getLongUrl();

        // If the original URL is not found, return a 404 error
        if (originalUrl == null)
            return ResponseEntity.notFound().build();

        analyticsService.updateAnalytics(shortLink);

        // Redirect to the original URL
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
