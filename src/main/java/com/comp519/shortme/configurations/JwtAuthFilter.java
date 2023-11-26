package com.comp519.shortme.configurations;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.comp519.shortme.constants.ApplicationConstants;
import com.comp519.shortme.dto.ExceptionResponseDto;
import com.comp519.shortme.exceptions.TokenNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.comp519.shortme.constants.ApplicationConstants.*;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthProvider userAuthProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException, JWTDecodeException, IllegalArgumentException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null) {
            String[] authElements = header.split(" ");

            if(authElements.length != 2)
                throw new JWTVerificationException(INVALID_TOKEN_EXCEPTION);

            if ("Bearer".equals(authElements[0])) {
                try {
                    SecurityContextHolder.getContext().setAuthentication(
                            userAuthProvider.validateToken(authElements[1]));
                }
                catch (JWTDecodeException e) {
                    setError(response, e.getMessage());
                    return;
                }
                catch (TokenExpiredException e) {
                    setError(response, e.getMessage());
                    return;
                }
                catch (RuntimeException e) {
                    SecurityContextHolder.clearContext();
                    throw e;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setError(HttpServletResponse response, String message) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ExceptionResponseDto responseDto = new ExceptionResponseDto(INVALID_JWT_MESSAGE, message, HttpStatus.UNAUTHORIZED.value());
        ObjectMapper mapper = new ObjectMapper();

        String jsonResponse = mapper.writeValueAsString(responseDto);
        response.getWriter().write(jsonResponse);
        return;
    }
}
