package com.tontine.customer.models.utils;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @NotNull(message = "street required")
    private String street;
    @NotNull(message = "zipCode required")
    private String zipCode;
    @NotNull(message = "city required")
    private String city;
    @NotNull(message = "state required")
    private String state;
    @NotNull(message = "country required")
    private String country;
}
