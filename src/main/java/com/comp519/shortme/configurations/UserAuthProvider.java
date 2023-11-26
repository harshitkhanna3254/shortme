package com.comp519.shortme.configurations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.comp519.shortme.dto.UserResponseDto;
import com.comp519.shortme.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    @Value("${security.jwt.token.secret-key}")
    private String jwtSecretKey;

    @PostConstruct
    protected void init() {
        jwtSecretKey = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
    }

    public String createToken(UserResponseDto userResponseDto) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3_600_600);

        return JWT.create()
                .withIssuer(userResponseDto.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("firstName", userResponseDto.getFirstName())
                .withClaim("lastName", userResponseDto.getLastName())
                .sign(Algorithm.HMAC256(jwtSecretKey));
    }

    public Authentication validateToken(String token) throws RuntimeException, IllegalArgumentException {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .username(decodedJWT.getIssuer())
                .firstName(decodedJWT.getClaim("firstName").asString())
                .lastName(decodedJWT.getClaim("lastName").asString())
                .build();

        return new UsernamePasswordAuthenticationToken(userResponseDto, null, Collections.emptyList());
    }
}
