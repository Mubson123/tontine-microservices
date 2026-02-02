package com.tontine.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
public class JwtService {
    @Value("${jwt.secret}")
    String jwtSecret;
    @Value("${jwt.expiration-time}")
    Long jwtExpirationMs;
    @Value("${jwt.refresh-expiration-time}")
    Long jwtRefreshExpirationMs;

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        return buildToken(userDetails, jwtExpirationMs);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, jwtRefreshExpirationMs);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userEmail = extractUsername(token);
        return (userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Instant extractExpirationInstant(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .toInstant();
    }

    public Integer extractExpirationInMinutes(String token) {
        Date expirationDate = extractAllClaims(token).getExpiration();
        long nowMillis = System.currentTimeMillis();
        long expMillis = expirationDate.getTime();
        long diffMillis = expMillis - nowMillis;
        return (int) (diffMillis / (1000 * 60));
    }

    private String buildToken(UserDetails userDetails, Long expiration) {
        Date now = new Date();
        return Jwts.builder()
                .header()
                .add("alg", "HS256")
                .add("typ", "JWT")
                .and()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
