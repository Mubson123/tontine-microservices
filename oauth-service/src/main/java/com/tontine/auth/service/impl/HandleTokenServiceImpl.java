package com.tontine.auth.service.impl;


import com.tontine.auth.model.*;
import com.tontine.auth.models.RefreshToken;
import com.tontine.auth.models.User;
import com.tontine.auth.repository.RefreshTokenRepository;
import com.tontine.auth.repository.UserRepository;
import com.tontine.auth.service.HandleTokenService;
import com.tontine.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HandleTokenServiceImpl implements HandleTokenService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public ApiTokenResponse login(ApiLoginRequest request) {

        RefreshToken existingToken = refreshTokenRepository
                .findByUsername(request.getUsername())
                .orElse(null);
        if (existingToken != null && existingToken.isRevoked()) {
            refreshTokenRepository.delete(existingToken);
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setUsername(request.getUsername());
        tokenEntity.setToken(refreshToken);
        tokenEntity.setExpiryDate(jwtService.extractExpirationInstant(refreshToken));
        tokenEntity.setCreatedAt(LocalDateTime.now());
        tokenEntity.setUpdatedAt(LocalDateTime.now());
        refreshTokenRepository.save(tokenEntity);
        int expireIn = jwtService.extractExpirationInMinutes(refreshToken);
        return new ApiTokenResponse(accessToken, refreshToken, expireIn);
    }

    public ApiTokenResponse refreshAccessToken(ApiRefreshTokenRequest request) {
        String token = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String username = jwtService.extractUsername(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtService.generateToken(user);
        int expireIn = jwtService.extractExpirationInMinutes(token);
        return new ApiTokenResponse(newAccessToken, token, expireIn);
    }

    @Transactional
    public void resetPassword(String accessToken, ApiPasswordResetRequest passwordReset) {
        String username = jwtService.extractUsername(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtService.isTokenValid(accessToken, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username %s not found", username)));
        user.setPassword(passwordReset.getNewPassword());
        user.setMustChangePassword(false);
        userRepository.save(user);
    }

    @Transactional
    public void logout(ApiLogoutRequest request) {
        String token = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        if (refreshToken.isRevoked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token already revoked");
        }
        String username = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void logoutAll(ApiLogoutRequest request) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        refreshTokenRepository.deleteAllByUsername(refreshToken.getUsername());
    }
}
