package com.tontine.customer.controller;

import com.tontine.customer.AdminCustomerControllerApi;
import com.tontine.customer.model.ApiStatusResponse;
import com.tontine.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminCustomerController implements AdminCustomerControllerApi {

    private final CustomerService customerService;

    @Override
    public ResponseEntity<ApiStatusResponse> activateCustomer(UUID customerId) {
        return ResponseEntity.accepted().body(customerService.activateCustomer(customerId));
    }

    @Override
    public ResponseEntity<ApiStatusResponse> deactivateCustomer(UUID customerId) {
        return ResponseEntity.accepted().body(customerService.deactivateCustomer(customerId));
    }

    @Override
    public ResponseEntity<ApiStatusResponse> suspendCustomer(UUID customerId) {
        return ResponseEntity.accepted().body(customerService.suspendCustomer(customerId));
    }
}
