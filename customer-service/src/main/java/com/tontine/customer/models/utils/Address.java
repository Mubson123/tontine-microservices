package com.tontine.customer.models.utils;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.tontine.customer.constance.Constance.REQUIRED;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Size(min = 3, message = "street " + REQUIRED)
    private String street;
    @Size(min = 5, max = 5, message = "zipCode must be a 5-digit number")
    private String zipCode;
    @Size(min = 3, message = "city " + REQUIRED)
    private String city;
    @Size(min = 3, message = "state " + REQUIRED)
    private String state;
    @Size(min = 3, message = "country " + REQUIRED)
    private String country;
}
