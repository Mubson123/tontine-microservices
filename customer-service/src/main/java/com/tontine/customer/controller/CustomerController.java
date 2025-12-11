package com.tontine.customer.controller;

import com.tontine.customer.CustomerControllerApi;
import com.tontine.customer.model.ApiCustomerRequest;
import com.tontine.customer.model.ApiCustomerResponse;
import com.tontine.customer.model.ApiProfile;
import com.tontine.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController implements CustomerControllerApi {
    private final CustomerService customerService;

    @Override
    public ResponseEntity<List<ApiCustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getCustomers());
    }

    @Override
    public ResponseEntity<ApiCustomerResponse> getCustomerById(UUID customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @Override
    public ResponseEntity<ApiCustomerResponse> createCustomer(ApiCustomerRequest apiCustomerRequest) {
        ApiCustomerResponse response = customerService.createCustomer(apiCustomerRequest);
        String id = response.getId().toString();
        URI location = UriComponentsBuilder.fromUriString("/api/customers/" + id).build().toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<ApiCustomerResponse> updateCustomer(UUID customerId, ApiCustomerRequest apiCustomerRequest) {
        return ResponseEntity.accepted().body(customerService.updateCustomer(customerId, apiCustomerRequest));
    }

    @Override
    public ResponseEntity<ApiCustomerResponse> updateCustomerProfile(UUID customerId, ApiProfile apiProfile) {
        return ResponseEntity.accepted().body(customerService.updateProfile(customerId, apiProfile));
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(UUID customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
