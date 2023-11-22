package com.comp519.shortme.utils;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class Utils {

    public String generateShortLink() {
        UUID uuid = UUID.randomUUID();
        String base62Uuid = toBase62(uuid);
        return pickRandom7Chars(base62Uuid);
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
