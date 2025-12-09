package com.tontine.customer.models;

import com.tontine.customer.models.utils.DocumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class IdDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @org.hibernate.validator.constraints.UUID
    private UUID customerId;
    @NotBlank(message = "nationality required")
    private String nationality;
    @NotBlank(message = "document number required")
    private String documentNumber;
    @NotNull(message = "issue date required")
    private LocalDate issueDate;
    @NotNull(message = "expiry date required")
    private LocalDate expiryDate;
    @NotBlank(message = "issued by required")
    private String issuedBy;
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
}
