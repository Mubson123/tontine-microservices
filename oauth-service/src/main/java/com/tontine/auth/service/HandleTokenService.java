package com.tontine.auth.service;

import com.tontine.auth.model.*;

public interface HandleTokenService {
    ApiTokenResponse login(ApiLoginRequest request);
    ApiTokenResponse refreshAccessToken(ApiRefreshTokenRequest request);
    void resetPassword(String token, ApiPasswordResetRequest passwordReset);
    void logout(ApiLogoutRequest request);
    void logoutAll(ApiLogoutRequest request);
}
