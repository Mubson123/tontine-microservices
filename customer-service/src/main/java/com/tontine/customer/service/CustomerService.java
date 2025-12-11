package com.tontine.customer.service;

import com.tontine.customer.model.ApiCustomerRequest;
import com.tontine.customer.model.ApiCustomerResponse;
import com.tontine.customer.model.ApiProfile;
import com.tontine.customer.model.ApiStatusResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<ApiCustomerResponse> getCustomers();
    ApiCustomerResponse getCustomerById(UUID customerId);
    ApiCustomerResponse createCustomer(ApiCustomerRequest apiCustomerRequest);
    ApiCustomerResponse updateCustomer(UUID customerId, ApiCustomerRequest apiCustomerRequest);
    ApiCustomerResponse updateProfile(UUID customerId, ApiProfile profile);
    ApiStatusResponse suspendCustomer(UUID customerId);
    ApiStatusResponse activateCustomer(UUID customerId);
    ApiStatusResponse deactivateCustomer(UUID customerId);
    void deleteCustomer(UUID customerId);
}
