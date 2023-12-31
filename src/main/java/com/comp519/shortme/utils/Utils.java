package com.comp519.shortme.utils;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.comp519.shortme.dto.ExceptionResponseDto;
import com.comp519.shortme.exceptions.InvalidUrlException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.comp519.shortme.constants.ApplicationConstants.INVALID_JWT_MESSAGE;

@Component
public class Utils {

    public String generateShortLink() {
        UUID uuid = UUID.randomUUID();
        String base62Uuid = toBase62(uuid);
        return pickRandom7Chars(base62Uuid);
    }

    public String retrieveShortLinkFromUrl(String shortUrl) throws MalformedURLException {
        URL url = new URL(shortUrl);
        String path = url.getPath();

        // Remove the leaving `/`
        return path.substring(1);
    }

    public static String convertShortLinkToUrl(String shortLink) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return baseUrl + "/" + shortLink;
    }

    public static List<String> convertShortLinksToUrls(List<String> shortLinks) {
        return shortLinks.stream()
                .map(Utils::convertShortLinkToUrl)
                .collect(Collectors.toList());
    }

    private final String BASE_62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String toBase62(UUID uuid) {
        // Combine both parts of the UUID into one BigInteger assuming they are unsigned.
        BigInteger mostSignificantBits = new BigInteger(Long.toUnsignedString(uuid.getMostSignificantBits()));
        BigInteger leastSignificantBits = new BigInteger(Long.toUnsignedString(uuid.getLeastSignificantBits()));

        // Shift the most significant bits to the left by 64 bits and add the least significant bits.
        BigInteger totalBits = mostSignificantBits.shiftLeft(64).or(leastSignificantBits);

        BigInteger base = BigInteger.valueOf(62);
        StringBuilder encoded = new StringBuilder();

        // Convert to base 62 string.
        while (totalBits.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = totalBits.divideAndRemainder(base);
            encoded.insert(0, BASE_62.charAt(divmod[1].intValue()));
            totalBits = divmod[0];
        }

        // While loop ends: totalBits is zero now, encoded is the base62 representation.
        // Ensure that the string is the correct length, padding with zeros if necessary.
        while (encoded.length() < 11) { // UUIDs in base 62 should be at least 11 characters long.
            encoded.insert(0, '0');
        }

        return encoded.toString();
    }


    public String pickRandom7Chars(String base62Uuid) {
        if (base62Uuid.length() <= 7) {
            return base62Uuid;
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder shortUrl = new StringBuilder(7);
        for (int i = 0; i < 7; i++) {
            int randomIndex = random.nextInt(base62Uuid.length());
            shortUrl.append(base62Uuid.charAt(randomIndex));
        }
        return shortUrl.toString();
    }
}
