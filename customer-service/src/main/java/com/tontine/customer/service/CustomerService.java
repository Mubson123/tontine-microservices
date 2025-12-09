package com.tontine.customer.service;

import com.tontine.customer.model.ApiCustomerRequest;
import com.tontine.customer.model.ApiCustomerResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<ApiCustomerResponse> getCustomers();
    ApiCustomerResponse getCustomerById(UUID customerId);
    ApiCustomerResponse createCustomer(ApiCustomerRequest apiCustomerRequest);
    ApiCustomerResponse updateCustomer(UUID customerId, ApiCustomerRequest apiCustomerRequest);
    void deleteCustomer(UUID customerId);
}
