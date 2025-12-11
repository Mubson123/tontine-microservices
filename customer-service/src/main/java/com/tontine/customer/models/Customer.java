package com.tontine.customer.models;

import com.tontine.customer.constance.Constance;
import com.tontine.customer.models.utils.*;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @NotNull(message = "title required")
    @Enumerated(EnumType.STRING)
    private Title title;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Size(min = 3, message = "firstname required or must have at least 3 characters")
    private String firstname;
    @Size(min = 3, message = "lastname required or must have at least 3 characters")
    private String lastname;
    @Email(message = "email should be valid", regexp = Constance.REGEX)
    private String email;
    @NotNull(message = "birthdate required")
    private LocalDate birthdate;
    @NotBlank(message = "phone required")
    private String phone;
    @NotNull(message = "maritalStatus required")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    @NotNull(message = "status required")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Valid
    @NotNull(message = "address required")
    @Embedded
    private Address address;

    public void updateProfile(
            String firstname,
            String lastname,
            String email,
            String phone,
            MaritalStatus maritalStatus,
            Status status,
            Address address
    ) {
        if (status == Status.SUSPENDED) {
            throw new IllegalArgumentException("Cannot update profile of a suspended customer");
        }
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.maritalStatus = maritalStatus;
        this.status = status;
        this.address = address;
    }

    public void deactivate() {
        if (status == Status.INACTIVE) {
            throw new IllegalArgumentException("Customer already inactive");
        } else if (status == Status.SUSPENDED) {
            throw new IllegalArgumentException("Customer already suspended");
        }
        this.status = Status.INACTIVE;
    }

    public void suspend() {
        if (status == Status.SUSPENDED) {
            throw new IllegalArgumentException("Customer already suspended");
        }
        this.status = Status.SUSPENDED;
    }

    public void activate() {
        if (status == Status.ACTIVE) {
            throw new IllegalArgumentException("Customer already active");
        }
        this.status = Status.ACTIVE;
    }
}
