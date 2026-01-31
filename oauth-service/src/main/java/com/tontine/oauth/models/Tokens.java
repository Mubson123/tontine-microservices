package com.tontine.oauth.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String email;
    private Boolean isValidToken;
    @Column(name = "access_token", columnDefinition = "TEXT")
    private String accessToken;
    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    public Tokens(String email, String accessToken, String refreshToken) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isValidToken = Boolean.TRUE;
    }

    public void invalidate() {
        this.isValidToken = Boolean.FALSE;
    }
}
