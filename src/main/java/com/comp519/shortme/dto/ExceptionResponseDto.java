package com.comp519.shortme.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ExceptionResponseDto {
    private String errorSummary; // A brief, human-readable summary of the error
    private String detailedMessage; // Detailed message, usually the actual exception message
    private int statusCode; // HTTP status code
    private String timestamp; // Time of error occurrence

    public ExceptionResponseDto(String errorSummary, String detailedMessage, int statusCode) {
        this.errorSummary = errorSummary;
        this.detailedMessage = detailedMessage;
        this.statusCode = statusCode;
        this.timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
    }
}
