package com.example.blackninja.dtos.request;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter @NoArgsConstructor
public class RefreshTokenRequest {

    @NotNull(message = "refresh token cannot be null")
    private String refreshToken;

    @NotNull(message = "access token cannot be null")
    private String accessToken;

    public RefreshTokenRequest(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
