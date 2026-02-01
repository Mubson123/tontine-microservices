package com.tontine.oauth.service;

import com.tontine.oauth.models.RefreshToken;
import com.tontine.oauth.models.dto.AuthRequest;
import com.tontine.oauth.models.dto.AuthResponse;
import com.tontine.oauth.models.dto.LogoutRequest;
import com.tontine.oauth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class HandleTokenService {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword())
        );
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setToken(refreshToken);
        tokenEntity.setUsername(user.getUsername());
        tokenEntity.setExpiryDate(jwtService.extractExpirationInstant(refreshToken));
        refreshTokenRepository.save(tokenEntity);
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(RefreshToken request) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String newAccessToken = jwtService.generateToken(user);
        return new AuthResponse(newAccessToken, refreshToken.getToken());
    }

    @Transactional
    public void logout(LogoutRequest request) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void logoutAll(Authentication authentication) {
        String username = authentication.getName();
        refreshTokenRepository.deleteAllByUsername(username);
    }
}
