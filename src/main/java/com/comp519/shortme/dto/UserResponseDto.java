package com.comp519.shortme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    // JWT token
    private String token;

    // Fields for account_info
    private String username;
    private String firstName;
    private String lastName;
    private String password; // Note: Storing passwords as plain text is not recommended
    private String createdAt;

    // Fields for subscription_info
    private String subscriptionPlan;
    private String subscriptionStartDate;
    private String subscriptionEndDate;

    // List of URLs (assuming they are stored as separate strings)
    private List<String> urls;

    // Helper method to parse LocalDateTime (Not needed as of now)
    public static LocalDateTime parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateString, formatter);
    }
}
