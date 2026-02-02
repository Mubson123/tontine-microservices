package com.tontine.auth.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.tontine.auth.constance.Constance.REGEX;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @NotBlank(message = "Token is required")
    @Column(unique = true, nullable = false)
    private String token;
    @NotBlank(message = "Email required")
    @Email(message = "Email format not correct", regexp = REGEX)
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private boolean revoked = false;
    @Column(nullable = false)
    private Instant expiryDate;
}
