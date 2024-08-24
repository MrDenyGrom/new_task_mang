package com.example.taskmanagement.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
                .compact();

        logger.info("Token generated for user: {}", username);
        return token;
    }

    public String getUsernameFromJWT(String token) {
        String username = parseClaimsJws(token).getBody().getSubject();
        logger.info("Extracted username from token: {}", username);
        return username;
    }

    public boolean validateToken(String authToken) {
        try {
            parseClaimsJws(authToken);
            logger.info("Token is valid");
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            logger.error("Invalid token: {}", ex.getMessage());
            return false;
        }
    }

    private Jws<Claims> parseClaimsJws(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token);
    }
}
