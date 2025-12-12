package com.tontine.customer.models;

import com.tontine.customer.models.utils.MemberRole;
import com.tontine.customer.models.utils.Status;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    @NotNull(message = "customer required")
    private UUID customerId;
    @NotNull(message = "Tontine ID required")
    private UUID tontineId;
    @NotNull(message = "Join date required")
    private LocalDate joinedAt;
    @Nullable
    private LocalDate leftAt;
    @NotNull(message = "memberRole required")
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;
    @Min(value = 1, message = "position in rotation must be greater than 0")
    private int positionInRotation;
    @NotNull(message = "member status required")
    @Enumerated(EnumType.STRING)
    private Status status;

    public void assignRole(MemberRole newMemberRole) {
        if (status == Status.INACTIVE || status == Status.SUSPENDED) {
            throw new IllegalStateException("Cannot assign role to an inactive membership");
        }
        this.memberRole = newMemberRole;
    }

    public void changeRotationPosition(int newPosition) {
        if (newPosition < 1) {
            throw new IllegalArgumentException("Position in rotation must be a positive integer");
        }
        this.positionInRotation = newPosition;
    }
}
