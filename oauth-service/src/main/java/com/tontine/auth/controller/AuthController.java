package com.tontine.auth.controller;

import com.tontine.auth.AuthActionApi;
import com.tontine.auth.model.*;
import com.tontine.auth.service.HandleTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController implements AuthActionApi {
    private final HandleTokenService tokenService;

    @Override
    public ResponseEntity<ApiTokenResponse> login(ApiLoginRequest request) {
        return ResponseEntity.ok(tokenService.login(request));
    }

    @Override
    public ResponseEntity<ApiTokenResponse> refreshAccessToken(ApiRefreshTokenRequest request) {
        return ResponseEntity.ok(tokenService.refreshAccessToken(request));
    }

    @Override
    public ResponseEntity<Void> logout(ApiLogoutRequest request) {
        tokenService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> resetPassword(String token, ApiPasswordResetRequest passwordResetRequest) {
        tokenService.resetPassword(token, passwordResetRequest);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> logoutAll(ApiLogoutRequest request) {
        tokenService.logoutAll(request);
        return ResponseEntity.noContent().build();
    }
}
