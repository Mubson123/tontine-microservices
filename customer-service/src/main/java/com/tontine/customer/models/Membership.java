package com.tontine.customer.models;

import com.tontine.customer.models.utils.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    @NotNull(message = "Tontine ID required")
    private UUID tontineId;
    @NotNull(message = "Join date required")
    private LocalDate joinDate;
    @Nullable
    private LocalDate leftDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int positionInRotation;
    private boolean active;

    public void assignRole(Role newRole) {
        if (!active) {
            throw new IllegalStateException("Cannot assign role to an inactive membership.");
        }
        this.role = newRole;
    }

    public void changeRotationPosition(int newPosition) {
        if (newPosition < 1) {
            throw new IllegalArgumentException("Position in rotation must be a positive integer.");
        }
        this.positionInRotation = newPosition;
    }
}
