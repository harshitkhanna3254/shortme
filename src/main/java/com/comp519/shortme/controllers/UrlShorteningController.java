package com.comp519.shortme.controllers;

import com.comp519.shortme.dto.AllUrlsResponseDto;
import com.comp519.shortme.dto.UrlRequestDto;
import com.comp519.shortme.dto.UrlResponseDto;
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
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    public ResponseEntity<UrlResponseDto> shortenUrl(@RequestBody @Valid UrlRequestDto request) throws Exception {
        String longUrl = request.getUrl();

        // Generate the short link
        String shortLink = utils.generateShortLink();

        // Save to Big Table
        UrlResponseDto urlResponseDto = urlShorteningService.saveUrlMapping(shortLink, longUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body(urlResponseDto);
    }

    // API to retrieve the original URL
    @GetMapping("/retrieve")
    public ResponseEntity<UrlResponseDto> retrieveUrl(@ModelAttribute @Valid UrlRequestDto urlRequestDto)
            throws MalformedURLException, NotFoundException, ExecutionException, InterruptedException {

        String shortUrl = urlRequestDto.getUrl();
        String shortLink = utils.retrieveShortLinkFromUrl(shortUrl);

        UrlResponseDto urlResponseDto = urlShorteningService.retrieveOriginalUrl(shortLink);

        // If the long URL is found, return it in the response.
        return ResponseEntity.ok(urlResponseDto);
    }

    // API to get all the URLs of the current logged in user
    @GetMapping("/retrieveAll")
    public ResponseEntity<AllUrlsResponseDto> retrieveAllUrls() {
        AllUrlsResponseDto allUrlsResponseDto = urlShorteningService.retrieveAllUrlsOfUser();

        return ResponseEntity.ok(allUrlsResponseDto);
    }
}
