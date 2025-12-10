package com.tontine.customer.fixtures;

import com.tontine.customer.model.*;
import com.tontine.customer.models.Customer;
import com.tontine.customer.models.utils.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CustomerFixtures {
    private CustomerFixtures() {
        throw new IllegalStateException(CustomerFixtures.class.getName());
    }

    public static ApiCustomerRequest apiCustomerRequest1() {
        return new ApiCustomerRequest()
                .title(ApiTitle.BSC)
                .gender(ApiGender.MALE)
                .firstname("John")
                .lastname("Doe")
                .birthdate(LocalDate.of(1990, 1, 1))
                .email("john.doe@example.com")
                .phone("+1234567890")
                .maritalStatus(ApiMaritalStatus.SINGLE)
                .address(new ApiAddress()
                        .street("123 Main St")
                        .city("Anytown")
                        .state("Anystate")
                        .zipCode("12345")
                        .country("USA")
                );
    }

    public static Customer customer1() {
        return Customer.builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.of(2023, 8, 14, 10, 50))
                .updatedAt(LocalDateTime.of(2024, 5, 6, 15, 37))
                .title(Title.BSC)
                .gender(Gender.MALE)
                .firstname("John")
                .lastname("Doe")
                .birthdate(LocalDate.of(1990, 1, 1))
                .email("john.doe@example.com")
                .phone("+1234567890")
                .maritalStatus(MaritalStatus.SINGLE)
                .status(Status.ACTIVE)
                .address(Address.builder()
                        .street("123 Main St")
                        .city("Anytown")
                        .state("Anystate")
                        .zipCode("12345")
                        .country("USA")
                        .build())
                .build();
    }

    public static ApiCustomerResponse apiCustomerResponse1() {
        return new ApiCustomerResponse()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.of(2023, 8, 14, 10, 50))
                .updatedAt(LocalDateTime.of(2024, 5, 6, 15, 37))
                .title(ApiTitle.BSC)
                .gender(ApiGender.MALE)
                .firstname("John")
                .lastname("Doe")
                .birthdate(LocalDate.of(1990, 1, 1))
                .email("john.doe@example.com")
                .phone("+1234567890")
                .maritalStatus(ApiMaritalStatus.SINGLE)
                .status(ApiStatus.ACTIVE)
                .address(new ApiAddress()
                        .street("123 Main St")
                        .city("Anytown")
                        .state("Anystate")
                        .zipCode("12345")
                        .country("USA")
                );
    }

    public static ApiCustomerRequest apiCustomerRequest2() {
        return new ApiCustomerRequest()
                .title(ApiTitle.MSC)
                .gender(ApiGender.FEMALE)
                .firstname("Sophia")
                .lastname("Smith")
                .birthdate(LocalDate.of(1985, 7, 20))
                .email("sophia.Smith@example.com")
                .phone("+1987654321")
                .maritalStatus(ApiMaritalStatus.SINGLE)
                .address(new ApiAddress()
                        .street("456 Elm St")
                        .city("Othertown")
                        .state("Otherstate")
                        .zipCode("67890")
                        .country("USA")
                );

    }

    public static Customer customer2() {
        return Customer.builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.of(2023, 8, 10, 12, 25))
                .updatedAt(LocalDateTime.of(2024, 5, 6, 15, 16))
                .title(Title.MSC)
                .gender(Gender.FEMALE)
                .firstname("Sophia")
                .lastname("Smith")
                .birthdate(LocalDate.of(1985, 7, 20))
                .email("sophia.Smith@example.com")
                .phone("+1987654321")
                .maritalStatus(MaritalStatus.SINGLE)
                .status(Status.ACTIVE)
                .address(Address.builder()
                        .street("456 Elm St")
                        .city("Othertown")
                        .state("Otherstate")
                        .zipCode("67890")
                        .country("USA")
                        .build())
                .build();
    }

    public static ApiCustomerResponse apiCustomerResponse2() {
        return new ApiCustomerResponse()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.of(2023, 8, 10, 12, 25))
                .updatedAt(LocalDateTime.of(2024, 5, 6, 15, 37))
                .title(ApiTitle.MSC)
                .gender(ApiGender.FEMALE)
                .firstname("Sophia")
                .lastname("Smith")
                .birthdate(LocalDate.of(1985, 7, 20))
                .email("sophia.Smith@example.com")
                .phone("+1987654321")
                .maritalStatus(ApiMaritalStatus.SINGLE)
                .status(ApiStatus.ACTIVE)
                .address(new ApiAddress()
                        .street("456 Elm St")
                        .city("Othertown")
                        .state("Otherstate")
                        .zipCode("67890")
                        .country("USA")
                );
    }

    public static List<Customer> customerList = List.of(customer1(), customer2());
    public static List<ApiCustomerResponse> responseList = List.of(apiCustomerResponse1(), apiCustomerResponse2());

    public static Customer wrongCustomer() {
        return Customer.builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.of(2023, 8, 14, 10, 50))
                .updatedAt(LocalDateTime.of(2024, 5, 6, 15, 37))
                // Title is missing
                .gender(Gender.MALE)
                .firstname("") // Invalid: empty
                .lastname("Doe")
                .email("john.doe@example") // Invalid email
                // Birthdate is missing
                .phone("+1234567890")
                .status(Status.ACTIVE)
                .maritalStatus(MaritalStatus.SINGLE)
                .address(Address.builder()
                        .street("123 Main St")
                        .city("Anytown")
                        .state("Anystate")
                        .zipCode("12345")
                        .country("USA")
                        .build())
                .build();
    }

    public static Address wrongAddress() {
        return Address.builder()
                .street("12") // Invalid: less than 3 characters
                .city("") // Invalid: empty
                .state("") // Invalid: empty
                .zipCode("1234") // Invalid: not 5-digit
                .country("") // Invalid: empty
                .build();
    }
}